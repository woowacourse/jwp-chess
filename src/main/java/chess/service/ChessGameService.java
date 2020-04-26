package chess.service;

import chess.model.domain.board.CastlingElement;
import chess.model.domain.board.CastlingSetting;
import chess.model.domain.board.ChessBoard;
import chess.model.domain.board.ChessGame;
import chess.model.domain.board.EnPassant;
import chess.model.domain.board.Square;
import chess.model.domain.board.TeamScore;
import chess.model.domain.piece.Piece;
import chess.model.domain.piece.Team;
import chess.model.domain.piece.Type;
import chess.model.domain.state.MoveInfo;
import chess.model.domain.state.MoveState;
import chess.model.dto.ChessGameDto;
import chess.model.dto.GameInfoDto;
import chess.model.dto.GameResultDto;
import chess.model.dto.MoveDto;
import chess.model.dto.PathDto;
import chess.model.dto.PromotionTypeDto;
import chess.model.dto.SourceDto;
import chess.model.repository.ChessBoardDao;
import chess.model.repository.ChessGameDao;
import chess.model.repository.ChessResultDao;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ChessGameService {

    private static final ChessGameDao CHESS_GAME_DAO = ChessGameDao.getInstance();
    private static final ChessBoardDao CHESS_BOARD_DAO = ChessBoardDao.getInstance();
    private static final ChessResultDao CHESS_RESULT_DAO = ChessResultDao.getInstance();

    public Integer create(Integer roomId, Map<Team, String> userNames) {
        Integer gameId = saveNewGameInfo(userNames, roomId);
        saveNewUserNames(userNames);
        return gameId;
    }

    public Integer saveNewGameInfo(Map<Team, String> userNames, Integer roomId) {
        closeGamesOf(roomId);
        ChessGame chessGame = new ChessGame();
        Integer gameId = CHESS_GAME_DAO
            .create(roomId, chessGame.getTurn(), userNames, chessGame.deriveTeamScore());
        CHESS_BOARD_DAO.create(gameId, chessGame.getChessBoard(),
            makeCastlingElements(chessGame.getChessBoard(), chessGame.getCastlingElements()));
        return gameId;
    }

    public void saveNewUserNames(Map<Team, String> userNames) {
        Set<String> noExistingUserName = userNames.values().stream()
            .filter(userName -> !CHESS_RESULT_DAO.findUsers().contains(userName))
            .collect(Collectors.toSet());
        if (!noExistingUserName.isEmpty()) {
            CHESS_RESULT_DAO.createUserNames(noExistingUserName);
        }
    }

    public ChessGameDto move(MoveDto moveDTO) {
        Integer gameId = moveDTO.getGameId();
        ChessGame chessGame = findChessGame(gameId);
        MoveState moveState
            = chessGame.move(new MoveInfo(moveDTO.getSource(), moveDTO.getTarget()));
        Map<Team, String> userNames = CHESS_GAME_DAO.findUserNames(gameId);

        updateChessBoard(gameId, chessGame, moveState);
        boolean proceed = !updateResult(chessGame, moveState, userNames);
        updateGameInfo(gameId, chessGame, proceed);

        return new ChessGameDto(chessGame, moveState, chessGame.deriveTeamScore(), userNames);
    }

    private void updateGameInfo(Integer gameId, ChessGame chessGame, boolean proceed) {
        CHESS_GAME_DAO.update(gameId, chessGame.getTurn(), chessGame.deriveTeamScore(), proceed);
    }

    private boolean updateResult(ChessGame chessGame, MoveState moveState,
        Map<Team, String> userNames) {
        if (moveState == MoveState.KING_CAPTURED) {
            for (Team team : Team.values()) {
                setGameResult(chessGame.deriveTeamScore(), userNames, team);
            }
            return true;
        }
        return false;
    }

    private void updateChessBoard(Integer gameId, ChessGame chessGame, MoveState moveState) {
        if (moveState.isSucceed()) {
            CHESS_BOARD_DAO.delete(gameId);
            CHESS_BOARD_DAO.create(gameId, chessGame.getChessBoard(),
                makeCastlingElements(chessGame.getChessBoard(), chessGame.getCastlingElements()),
                chessGame.getEnPassants().entrySet().stream()
                    .collect(Collectors.toMap(Entry::getValue, Entry::getKey)));
        }
    }

    public void closeGamesOf(Integer roomId) {
        List<Integer> proceedGameIds = CHESS_GAME_DAO.findProceedGameIdsBy(roomId);
        for (Integer gameId : proceedGameIds) {
            closeGame(gameId);
        }
    }

    private Map<Square, Boolean> makeCastlingElements(Map<Square, Piece> chessBoard,
        Set<CastlingSetting> castlingElements) {
        return chessBoard.keySet().stream()
            .collect(Collectors.toMap(square -> square,
                square -> makeCastlingElements(square, chessBoard.get(square), castlingElements)));
    }

    private boolean makeCastlingElements(Square square, Piece piece,
        Set<CastlingSetting> castlingElements) {
        return castlingElements.stream()
            .anyMatch(castlingSetting -> castlingSetting.isCastlingBefore(square, piece));
    }

    public ChessGameDto loadChessGame(Integer gameId) {
        return new ChessGameDto(findChessGame(gameId), CHESS_GAME_DAO.findUserNames(gameId));
    }

    private ChessGame findChessGame(Integer gameId) {
        ChessBoard chessBoard = ChessBoard.of(CHESS_BOARD_DAO.findBoard(gameId));
        CastlingElement castlingElements = CastlingElement
            .of(CHESS_BOARD_DAO.findCastlingElements(gameId));
        EnPassant enPassant = CHESS_BOARD_DAO.findEnpassantBoard(gameId);
        Team gameTurn = CHESS_GAME_DAO.findCurrentTurn(gameId).orElseThrow(IllegalAccessError::new);
        return new ChessGame(chessBoard, gameTurn, castlingElements, enPassant);
    }

    public GameInfoDto closeGame(Integer gameId) {
        Optional<GameInfoDto> gameInfoDTO = CHESS_GAME_DAO.findGameInfo(gameId);
        GameInfoDto gameInfoDto = gameInfoDTO
            .orElseThrow(() -> new IllegalArgumentException("닫을 게임이 없습니다."));
        CHESS_GAME_DAO.updateProceedN(gameId);
        Map<Team, String> userNames = gameInfoDto.getUserNames();
        TeamScore teamScore = gameInfoDto.getTeamScores();
        for (Team team : Team.values()) {
            setGameResult(teamScore, userNames, team);
        }
        return gameInfoDto;
    }

    private void setGameResult(TeamScore teamScore, Map<Team, String> userNames, Team team) {
        GameResultDto gameResultBefore = CHESS_RESULT_DAO.findWinOrDraw(userNames.get(team))
            .orElseThrow(IllegalAccessError::new);
        GameResultDto gameResult = teamScore.getGameResult(team);
        GameResultDto gameResultAfter = new GameResultDto(
            gameResultBefore.getWinCount() + gameResult.getWinCount(),
            gameResultBefore.getDrawCount() + gameResult.getDrawCount(),
            gameResultBefore.getLoseCount() + gameResult.getLoseCount());
        CHESS_RESULT_DAO.update(userNames.get(team), gameResultAfter);
    }

    public ChessGameDto promote(PromotionTypeDto promotionTypeDTO) {
        Integer gameId = promotionTypeDTO.getGameId();
        ChessGame chessGame = findChessGame(gameId);
        MoveState moveState = chessGame.promote(Type.of(promotionTypeDTO.getPromotionType()));

        updateChessBoard(gameId, chessGame, moveState);
        updateGameInfo(gameId, chessGame, true);

        return new ChessGameDto(chessGame, moveState, chessGame.deriveTeamScore(),
            CHESS_GAME_DAO.findUserNames(gameId));
    }

    public PathDto findPath(SourceDto sourceDto) {
        ChessGame chessGame = findChessGame(sourceDto.getGameId());
        return new PathDto(chessGame.findMovableAreas(Square.of(sourceDto.getSource())));
    }

    public Integer createBy(Integer gameId, Map<Team, String> userNames) {
        return create(CHESS_GAME_DAO.findRoomId(gameId).orElseThrow(IllegalArgumentException::new),
            userNames);
    }

    public Integer findRoomId(Integer gameId) {
        return CHESS_GAME_DAO.findRoomId(gameId).orElseThrow(IllegalAccessError::new);
    }

    public Integer findIdBefore(Integer roomId, Map<Team, String> userNames) {
        Optional<Integer> gameNumberLatest = CHESS_GAME_DAO.findGameNumberLatest(roomId);
        if (gameNumberLatest.isPresent()) {
            return gameNumberLatest.get();
        }
        create(roomId, userNames);
        return CHESS_GAME_DAO.findGameNumberLatest(roomId).orElseThrow(IllegalAccessError::new);
    }

    public ChessGameDto endGame(Integer gameId) {
        GameInfoDto gameInfoDto = closeGame(gameId);
        return new ChessGameDto(gameInfoDto.getTeamScores(), gameInfoDto.getUserNames());
    }
}
