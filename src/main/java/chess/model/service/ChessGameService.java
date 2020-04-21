package chess.model.service;

import chess.model.domain.board.BoardInitialByDB;
import chess.model.domain.board.BoardInitialization;
import chess.model.domain.board.BoardSquare;
import chess.model.domain.board.CastlingSetting;
import chess.model.domain.board.ChessGame;
import chess.model.domain.board.EnPassant;
import chess.model.domain.board.TeamScore;
import chess.model.domain.piece.Color;
import chess.model.domain.piece.Pawn;
import chess.model.domain.piece.Piece;
import chess.model.domain.piece.Type;
import chess.model.domain.state.MoveOrder;
import chess.model.domain.state.MoveSquare;
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

public class ChessGameService {

    private static final ChessGameDao CHESS_GAME_DAO = ChessGameDao.getInstance();
    private static final ChessBoardDao CHESS_BOARD_DAO = ChessBoardDao.getInstance();
    private static final ChessResultDao CHESS_RESULT_DAO = ChessResultDao.getInstance();
    private static final ChessGameService INSTANCE = new ChessGameService();

    private ChessGameService() {
    }

    public static ChessGameService getInstance() {
        return INSTANCE;
    }

    public int getIdBefore(int roomId, Map<Color, String> userNames) {
        Optional<Integer> gameNumberLatest = CHESS_GAME_DAO.getGameNumberLatest(roomId);
        if (gameNumberLatest.isPresent()) {
            return gameNumberLatest.get();
        }
        newChessGame(roomId, userNames);
        return CHESS_GAME_DAO.getGameNumberLatest(roomId).orElseThrow(IllegalAccessError::new);
    }

    public int newChessGame(int roomId, Map<Color, String> userNames) {
        roomBeforeGameOver(roomId);
        ChessGame chessGame = new ChessGame();
        int gameId = CHESS_GAME_DAO
            .insert(roomId, chessGame.getGameTurn(), userNames, chessGame.getTeamScore());
        CHESS_BOARD_DAO.insert(gameId, chessGame.getChessBoard(),
            getCastlingElement(chessGame.getChessBoard(), chessGame.getCastlingElements()));
        CHESS_GAME_DAO.updateScore(gameId, chessGame.getTeamScore());
        for (String userName : userNames.values()) {
            if (!CHESS_RESULT_DAO.getWinOrDraw(userName).isPresent()) {
                CHESS_RESULT_DAO.insert(userName);
            }
        }
        return gameId;
    }

    public void roomBeforeGameOver(int roomId) {
        List<Integer> proceedGameIds = CHESS_GAME_DAO.getProceedGameIdsByRoomId(roomId);
        for (int gameId : proceedGameIds) {
            gameOver(gameId);
        }
    }

    private Map<BoardSquare, Boolean> getCastlingElement(Map<BoardSquare, Piece> chessBoard,
        Set<CastlingSetting> castlingElements) {
        return chessBoard.keySet().stream()
            .collect(Collectors.toMap(boardSquare -> boardSquare,
                boardSquare -> getCastlingElement(boardSquare, chessBoard.get(boardSquare),
                    castlingElements)));
    }

    private boolean getCastlingElement(BoardSquare boardSquare, Piece piece,
        Set<CastlingSetting> castlingElements) {
        return castlingElements.stream()
            .anyMatch(castlingSetting -> castlingSetting.isCastlingBefore(boardSquare, piece));
    }

    public ChessGameDto loadChessGame(int gameId) {
        return new ChessGameDto(getChessGame(gameId), CHESS_GAME_DAO.getUserNames(gameId));
    }

    private ChessGame getChessGame(int gameId) {
        BoardInitialization boardInitialByDB = new BoardInitialByDB(
            CHESS_BOARD_DAO.getBoard(gameId));
        Color gameTurn = CHESS_GAME_DAO.getGameTurn(gameId).orElseThrow(IllegalAccessError::new);
        Set<CastlingSetting> castlingElements = CHESS_BOARD_DAO.getCastlingElements(gameId);
        EnPassant enPassant = CHESS_BOARD_DAO.getEnpassantBoard(gameId);
        return new ChessGame(boardInitialByDB, gameTurn, castlingElements, enPassant);
    }

    public ChessGameDto move(MoveDto moveDTO) {
        MoveSquare moveSquare = new MoveSquare(moveDTO.getSource(), moveDTO.getTarget());
        int gameId = moveDTO.getGameId();
        EnPassant enPassant = CHESS_BOARD_DAO.getEnpassantBoard(gameId);
        ChessGame chessGame = getChessGame(gameId);
        boolean canCastling = chessGame.canCastling(moveSquare);
        boolean pawnSpecialMove = chessGame.isPawnSpecialMove(moveSquare);
        boolean movePawn = chessGame.whoMovePiece(moveSquare) instanceof Pawn;
        MoveState moveState = chessGame.movePieceWhenCanMove(moveSquare);
        Color gameTurn = CHESS_GAME_DAO.getGameTurn(gameId).orElseThrow(IllegalAccessError::new);
        if (moveState.isSucceed()) {
            CHESS_BOARD_DAO.deleteMyEnpassant(gameId);
            CHESS_BOARD_DAO.deleteBoardSquare(gameId, moveSquare.get(MoveOrder.AFTER));
            CHESS_BOARD_DAO.copyBoardSquare(gameId, moveSquare);
            CHESS_BOARD_DAO.deleteBoardSquare(gameId, moveSquare.get(MoveOrder.BEFORE));
            if (pawnSpecialMove) {
                CHESS_BOARD_DAO.updateEnPassant(gameId, moveSquare);
            }
            if (movePawn && enPassant
                .hasOtherEnpassant(moveSquare.get(MoveOrder.AFTER), gameTurn)) {
                CHESS_BOARD_DAO.deleteEnpassant(gameId, moveSquare.get(MoveOrder.AFTER));
            }
            if (canCastling) {
                MoveSquare moveSquareRook = CastlingSetting.getMoveCastlingRook(moveSquare);
                CHESS_BOARD_DAO.copyBoardSquare(gameId, moveSquareRook);
                CHESS_BOARD_DAO.deleteBoardSquare(gameId, moveSquareRook.get(MoveOrder.BEFORE));
            }
        }
        if (moveState == MoveState.SUCCESS) {
            CHESS_GAME_DAO.updateTurn(gameId, chessGame.getGameTurn());
        }
        if (moveState == MoveState.KING_CAPTURED) {
            gameOver(gameId);
        }
        CHESS_GAME_DAO.updateScore(gameId, chessGame.getTeamScore());
        return new ChessGameDto(getChessGame(gameId), moveState,
            new TeamScore(CHESS_GAME_DAO.getScores(gameId)), CHESS_GAME_DAO.getUserNames(gameId));
    }

    private void gameOver(int gameId) {
        if (CHESS_GAME_DAO.isProceed(gameId)) {
            CHESS_GAME_DAO.updateProceedN(gameId);
            Map<Color, String> userNames = CHESS_GAME_DAO.getUserNames(gameId);
            TeamScore teamScore = new TeamScore(CHESS_GAME_DAO.getScores(gameId));
            for (Color team : Color.values()) {
                setGameResult(teamScore, userNames, team);
            }
        }
    }

    private void setGameResult(TeamScore teamScore, Map<Color, String> userNames, Color team) {
        GameResultDto gameResultBefore = CHESS_RESULT_DAO.getWinOrDraw(userNames.get(team))
            .orElseThrow(IllegalAccessError::new);
        GameResultDto gameResult = teamScore.getGameResult(team);
        GameResultDto gameResultAfter = new GameResultDto(
            gameResultBefore.getWinCount() + gameResult.getWinCount(),
            gameResultBefore.getDrawCount() + gameResult.getDrawCount(),
            gameResultBefore.getLoseCount() + gameResult.getLoseCount());
        CHESS_RESULT_DAO.update(userNames.get(team), gameResultAfter);
    }

    public ChessGameDto promotion(PromotionTypeDto promotionTypeDTO) {
        Type type = Type.of(promotionTypeDTO.getPromotionType());
        int gameId = promotionTypeDTO.getGameId();
        ChessGame chessGame = getChessGame(gameId);
        Optional<BoardSquare> finishPawnBoard = chessGame.getFinishPawnBoard();
        Piece hopePiece = chessGame.getHopePiece(type);
        MoveState moveState = chessGame.promotion(type);
        if (moveState == MoveState.SUCCESS_PROMOTION) {
            CHESS_BOARD_DAO
                .updatePromotion(gameId, finishPawnBoard.orElseThrow(IllegalAccessError::new),
                    hopePiece);
            CHESS_GAME_DAO.updateTurn(gameId, chessGame.getGameTurn());
            CHESS_GAME_DAO.updateScore(gameId, chessGame.getTeamScore());
        }
        return new ChessGameDto(getChessGame(gameId), moveState,
            new TeamScore(CHESS_GAME_DAO.getScores(gameId)), CHESS_GAME_DAO.getUserNames(gameId));
    }

    public PathDto getPath(SourceDto sourceDto) {
        ChessGame chessGame = getChessGame(sourceDto.getGameId());
        return new PathDto(chessGame.getCheatSheet(BoardSquare.of(sourceDto.getSource())));
    }

    public ChessGameDto endGame(MoveDto moveDto) {
        int gameId = moveDto.getGameId();
        gameOver(gameId);
        return new ChessGameDto(new TeamScore(CHESS_GAME_DAO.getScores(gameId)),
            CHESS_GAME_DAO.getUserNames(gameId));
    }

    public int getRoomId(int gameId) {
        return CHESS_GAME_DAO.getRoomId(gameId).orElseThrow(IllegalAccessError::new);
    }

    public int endAndNewChessGame(int gameId, Map<Color, String> userNames) {
        gameOver(gameId);
        return newChessGame(
            CHESS_GAME_DAO.getRoomId(gameId).orElseThrow(IllegalArgumentException::new), userNames);
    }

}
