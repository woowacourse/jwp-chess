package chess.service;

import chess.model.domain.board.CastlingSetting;
import chess.model.domain.board.ChessBoard;
import chess.model.domain.board.ChessGame;
import chess.model.domain.board.EnPassant;
import chess.model.domain.board.Square;
import chess.model.domain.board.TeamScore;
import chess.model.domain.piece.Pawn;
import chess.model.domain.piece.Piece;
import chess.model.domain.piece.Team;
import chess.model.domain.piece.Type;
import chess.model.domain.state.MoveInfo;
import chess.model.domain.state.MoveOrder;
import chess.model.domain.state.MoveState;
import chess.model.dto.ChessGameDto;
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
        closeGamesOf(roomId);
        ChessGame chessGame = new ChessGame();
        Integer gameId = CHESS_GAME_DAO
            .insert(roomId, chessGame.getGameTurn(), userNames, chessGame.getTeamScore());

        CHESS_BOARD_DAO.insert(gameId, chessGame.getChessBoard(),
            getCastlingElement(chessGame.getChessBoard(), chessGame.getCastlingElements()));
        CHESS_GAME_DAO.updateScore(gameId, chessGame.getTeamScore());

        for (String userName : userNames.values()) {
            if (!CHESS_RESULT_DAO.findWinOrDraw(userName).isPresent()) {
                CHESS_RESULT_DAO.insert(userName);
            }
        }
        return gameId;
    }

    public void closeGamesOf(Integer roomId) {
        List<Integer> proceedGameIds = CHESS_GAME_DAO.findProceedGameIdsBy(roomId);
        for (Integer gameId : proceedGameIds) {
            closeGame(gameId);
        }
    }

    public void saveUser(Map<Team, String> userNames) {
        for (String userName : userNames.values()) {
            if (!CHESS_RESULT_DAO.findWinOrDraw(userName).isPresent()) {
                CHESS_RESULT_DAO.insert(userName);
            }
        }
    }

    public Integer saveRoom(Map<Team, String> userNames, Integer roomId) {
        closeGamesOf(roomId);
        ChessGame chessGame = new ChessGame();
        Integer gameId = CHESS_GAME_DAO
            .insert(roomId, chessGame.getGameTurn(), userNames, chessGame.getTeamScore());
        CHESS_BOARD_DAO.insert(gameId, chessGame.getChessBoard(),
            getCastlingElement(chessGame.getChessBoard(), chessGame.getCastlingElements()));
        CHESS_GAME_DAO.updateScore(gameId, chessGame.getTeamScore());
        return gameId;
    }

    private Map<Square, Boolean> getCastlingElement(Map<Square, Piece> chessBoard,
        Set<CastlingSetting> castlingElements) {
        return chessBoard.keySet().stream()
            .collect(Collectors.toMap(boardSquare -> boardSquare,
                boardSquare -> getCastlingElement(boardSquare, chessBoard.get(boardSquare),
                    castlingElements)));
    }

    private boolean getCastlingElement(Square square, Piece piece,
        Set<CastlingSetting> castlingElements) {
        return castlingElements.stream()
            .anyMatch(castlingSetting -> castlingSetting.isCastlingBefore(square, piece));
    }

    public ChessGameDto loadChessGame(Integer gameId) {
        return new ChessGameDto(getChessGame(gameId), CHESS_GAME_DAO.findUserNames(gameId));
    }

    private ChessGame getChessGame(Integer gameId) {
        ChessBoard chessBoard = ChessBoard.of(CHESS_BOARD_DAO.findBoard(gameId));
        Team gameTurn = CHESS_GAME_DAO.findCurrentTurn(gameId).orElseThrow(IllegalAccessError::new);
        Set<CastlingSetting> castlingElements = CHESS_BOARD_DAO.findCastlingElements(gameId);
        EnPassant enPassant = CHESS_BOARD_DAO.findEnpassantBoard(gameId);
        return new ChessGame(chessBoard, gameTurn, castlingElements, enPassant);
    }

    public ChessGameDto move(MoveDto moveDTO) {
        MoveInfo moveInfo = new MoveInfo(moveDTO.getSource(), moveDTO.getTarget());
        Integer gameId = moveDTO.getGameId();
        EnPassant enPassant = CHESS_BOARD_DAO.findEnpassantBoard(gameId);
        ChessGame chessGame = getChessGame(gameId);
        boolean canCastling = chessGame.canCastling(moveInfo);
        boolean pawnSpecialMove = chessGame.isPawnMoveTwoRankForward(moveInfo);
        boolean movePawn = chessGame.findPieceToMove(moveInfo) instanceof Pawn;
        MoveState moveState = chessGame.movePieceWhenCanMove(moveInfo);
        Team gameTurn = CHESS_GAME_DAO.findCurrentTurn(gameId).orElseThrow(IllegalAccessError::new);
        if (moveState.isSucceed()) {
            CHESS_BOARD_DAO.deleteMyEnpassant(gameId);
            CHESS_BOARD_DAO.deleteBoardSquare(gameId, moveInfo.get(MoveOrder.TO));
            CHESS_BOARD_DAO.copyBoardSquare(gameId, moveInfo);
            CHESS_BOARD_DAO.deleteBoardSquare(gameId, moveInfo.get(MoveOrder.FROM));
            if (pawnSpecialMove) {
                CHESS_BOARD_DAO.updateEnPassant(gameId, moveInfo);
            }
            if (movePawn && enPassant
                .hasOtherEnpassant(moveInfo.get(MoveOrder.TO), gameTurn)) {
                CHESS_BOARD_DAO.deleteEnpassant(gameId, moveInfo.get(MoveOrder.TO));
            }
            if (canCastling) {
                MoveInfo moveInfoRook = CastlingSetting.findMoveInfoOfRook(moveInfo);
                CHESS_BOARD_DAO.copyBoardSquare(gameId, moveInfoRook);
                CHESS_BOARD_DAO.deleteBoardSquare(gameId, moveInfoRook.get(MoveOrder.FROM));
            }

        }
        if (moveState == MoveState.SUCCESS) {
            CHESS_GAME_DAO.updateTurn(gameId, chessGame.getGameTurn());
        }
        if (moveState == MoveState.KING_CAPTURED) {
            closeGame(gameId);
        }
        CHESS_GAME_DAO.updateScore(gameId, chessGame.getTeamScore());
        return new ChessGameDto(getChessGame(gameId), moveState,
            new TeamScore(CHESS_GAME_DAO.findScores(gameId)), CHESS_GAME_DAO.findUserNames(gameId));
    }

    public void closeGame(Integer gameId) {
        if (CHESS_GAME_DAO.isProceed(gameId)) {
            CHESS_GAME_DAO.updateProceedN(gameId);
            Map<Team, String> userNames = CHESS_GAME_DAO.findUserNames(gameId);
            TeamScore teamScore = new TeamScore(CHESS_GAME_DAO.findScores(gameId));
            for (Team team : Team.values()) {
                setGameResult(teamScore, userNames, team);
            }
        }
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
        Type type = Type.of(promotionTypeDTO.getPromotionType());
        Integer gameId = promotionTypeDTO.getGameId();
        ChessGame chessGame = getChessGame(gameId);
        Square finishPawnBoard = chessGame.findSquareForPromote();
        Piece pieceToChange = chessGame.makePieceToChange(type);
        MoveState moveState = chessGame.promotion(type);
        if (moveState == MoveState.SUCCESS_PROMOTION) {
            CHESS_BOARD_DAO.updatePromotion(gameId, finishPawnBoard, pieceToChange);
            CHESS_GAME_DAO.updateTurn(gameId, chessGame.getGameTurn());
            CHESS_GAME_DAO.updateScore(gameId, chessGame.getTeamScore());
        }
        return new ChessGameDto(getChessGame(gameId), moveState,
            new TeamScore(CHESS_GAME_DAO.findScores(gameId)), CHESS_GAME_DAO.findUserNames(gameId));
    }

    public PathDto getPath(SourceDto sourceDto) {
        ChessGame chessGame = getChessGame(sourceDto.getGameId());
        return new PathDto(chessGame.getMovableArea(Square.of(sourceDto.getSource())));
    }

    public Integer createBy(Integer gameId, Map<Team, String> userNames) {
        return create(
            CHESS_GAME_DAO.findRoomId(gameId).orElseThrow(IllegalArgumentException::new),
            userNames);
    }

    public ChessGameDto endGame(Integer gameId) {
        closeGame(gameId);
        return new ChessGameDto(new TeamScore(CHESS_GAME_DAO.findScores(gameId)),
            CHESS_GAME_DAO.findUserNames(gameId));
    }

    public Integer getRoomId(Integer gameId) {
        return CHESS_GAME_DAO.findRoomId(gameId).orElseThrow(IllegalAccessError::new);
    }

    public Integer getIdBefore(Integer roomId, Map<Team, String> userNames) {
        Optional<Integer> gameNumberLatest = CHESS_GAME_DAO.findGameNumberLatest(roomId);
        if (gameNumberLatest.isPresent()) {
            return gameNumberLatest.get();
        }
        create(roomId, userNames);
        return CHESS_GAME_DAO.findGameNumberLatest(roomId).orElseThrow(IllegalAccessError::new);
    }

}
