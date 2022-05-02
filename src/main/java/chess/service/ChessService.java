package chess.service;

import java.util.List;
import java.util.Map.Entry;

import org.springframework.stereotype.Service;

import chess.dao.BoardDao;
import chess.dao.GameDao;
import chess.domain.board.Board;
import chess.domain.game.ChessGame;
import chess.domain.game.score.ScoreResult;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceColor;
import chess.domain.position.Position;
import chess.dto.request.CreatePieceDto;
import chess.dto.request.DeletePieceDto;
import chess.dto.request.UpdatePiecePositionDto;
import chess.entity.Room;

@Service
public class ChessService {
    private static final String ERROR_MESSAGE_NOT_END_GAME = "게임이 아직 안끝났습니다!";
    public static final String ERROR_MESSAGE_NOT_EQUAL_PASSWORD = "비밀번호가 일치하지 않습니다!";
    private final GameDao gameDao;
    private final BoardDao boardDao;

    public ChessService(GameDao gameDao, BoardDao boardDao) {
        this.gameDao = gameDao;
        this.boardDao = boardDao;
    }

    public int createGameAndGetId(String gameName, String gamePassword) {
        int id = gameDao.createGameAndGetId(gameName, gamePassword);
        createBoard(id);
        return id;
    }

    private void createBoard(int id) {
        Board initializedBoard = Board.createInitializedBoard();

        for (Entry<Position, Piece> entry : initializedBoard.getValue().entrySet()) {
            CreatePieceDto createPieceDto = CreatePieceDto.of(id, entry.getKey(), entry.getValue());
            boardDao.createPiece(createPieceDto);
        }
    }

    public void movePiece(UpdatePiecePositionDto updatePiecePositionDto) {
        int gameId = updatePiecePositionDto.getGameId();

        ChessGame chessGame = generateChessGame(gameId);
        chessGame.movePiece(updatePiecePositionDto.getFrom(), updatePiecePositionDto.getTo());

        updateGameTurn(gameId, chessGame);
        updatePiecePosition(updatePiecePositionDto, gameId);
    }

    private void updatePiecePosition(UpdatePiecePositionDto updatePiecePositionDto, int gameId) {
        boardDao.deletePiece(DeletePieceDto.of(gameId, updatePiecePositionDto.getTo()));
        boardDao.updatePiecePosition(updatePiecePositionDto);
    }

    private void updateGameTurn(int gameId, ChessGame chessGame) {
        if (chessGame.isWhiteTurn()) {
            gameDao.updateTurnToWhite(gameId);
            return;
        }

        gameDao.updateTurnToBlack(gameId);
    }

    public PieceColor getCurrentTurn(int gameId) {
        if (generateChessGame(gameId).isWhiteTurn()) {
            return PieceColor.WHITE;
        }
        return PieceColor.BLACK;
    }

    public ScoreResult getScore(int gameId) {
        return new ScoreResult(generateChessGame(gameId).getBoard());
    }

    public PieceColor getWinColor(int gameId) {
        ChessGame chessGame = generateChessGame(gameId);
        return chessGame.getWinColor();
    }

    private ChessGame generateChessGame(int gameId) {
        Board board = boardDao.getBoard(gameId);
        PieceColor gameTurn = gameDao.getGameTurn(gameId);

        return ChessGame.of(board, gameTurn);
    }

    public Board getBoard(int gameId) {
        return boardDao.getBoard(gameId);
    }

    public List<Room> getRooms() {
        return gameDao.inquireAllRooms();
    }

    public void deleteRoom(int gameId, String inputPassword) {
        checkSamePassword(gameId, inputPassword);
        checkGameIsEnd(gameId);
        boardDao.deletePieces(gameId);
        gameDao.deleteGame(gameId);
    }

    private void checkGameIsEnd(int gameId) {
        ChessGame chessGame = generateChessGame(gameId);
        if (!chessGame.isEnd()) {
            throw new IllegalArgumentException(ERROR_MESSAGE_NOT_END_GAME);
        }
    }

    private void checkSamePassword(int gameId, String inputPassword) {
        if (!inputPassword.equals(gameDao.getPasswordById(gameId))) {
            throw new IllegalArgumentException(ERROR_MESSAGE_NOT_EQUAL_PASSWORD);
        }
    }

    @Override
    public String toString() {
        return "ChessService{" +
            "gameDao=" + gameDao +
            ", boardDao=" + boardDao +
            '}';
    }

}
