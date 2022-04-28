package chess.service;

import chess.dao.BoardDao;
import chess.dao.GameRoomDao;
import chess.domain.board.Board;
import chess.domain.game.ChessGame;
import chess.domain.game.room.Room;
import chess.domain.game.room.RoomId;
import chess.domain.game.room.RoomPassword;
import chess.domain.game.score.Score;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceColor;
import chess.domain.position.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map.Entry;

@Service
public class ChessService {
    private final GameRoomDao gameRoomDao;
    private final BoardDao boardDao;

    @Autowired
    public ChessService(GameRoomDao gameRoomDao, BoardDao boardDao) {
        this.gameRoomDao = gameRoomDao;
        this.boardDao = boardDao;
    }

    public void createGame(Room room) {
        gameRoomDao.createGameRoom(room);

        Board initializedBoard = Board.createInitializedBoard();
        for (Entry<Position, Piece> entry : initializedBoard.getValue().entrySet()) {
            Position position = entry.getKey();
            Piece piece = entry.getValue();

            boardDao.createPiece(room.getId(), position, piece);
        }
    }

    public void deleteRoom(RoomId roomId, RoomPassword roomPassword) {
        if (!generateChessGame(roomId).isEnd()) {
            throw new IllegalStateException("게임이 끝나지 않아서 방을 삭제할 수 없습니다.");
        }

        gameRoomDao.deleteGameRoom(roomId, roomPassword);
    }

    public List<Room> getRooms() {
        return gameRoomDao.getRooms();
    }

    public void movePiece(RoomId roomId, Position from, Position to) {
        ChessGame chessGame = generateChessGame(roomId);
        chessGame.movePiece(from, to);

        updateGameTurn(roomId, chessGame);
        updatePiecePosition(roomId, from, to);
    }

    private void updatePiecePosition(RoomId roomId, Position from, Position to) {
        boardDao.deletePiece(roomId, to);
        boardDao.updatePiecePosition(roomId, from, to);
    }

    private void updateGameTurn(RoomId roomId, ChessGame chessGame) {
        if (chessGame.isWhiteTurn()) {
            gameRoomDao.updateTurnToWhite(roomId);
            return;
        }

        gameRoomDao.updateTurnToBlack(roomId);
    }

    public PieceColor getCurrentTurn(RoomId roomId) {
        ChessGame chessGame = generateChessGame(roomId);
        if (chessGame.isWhiteTurn()) {
            return PieceColor.WHITE;
        }

        return PieceColor.BLACK;
    }

    public Score getScore(RoomId roomId, PieceColor pieceColor) {
        ChessGame chessGame = generateChessGame(roomId);
        return chessGame.getStatus().getScoreByPieceColor(pieceColor);
    }

    public PieceColor getWinColor(RoomId roomId) {
        ChessGame chessGame = generateChessGame(roomId);
        return chessGame.getWinColor();
    }

    private ChessGame generateChessGame(RoomId roomId) {
        Board board = boardDao.getBoard(roomId);
        PieceColor currentTurn = gameRoomDao.getCurrentTurn(roomId);
        return ChessGame.of(board, currentTurn);
    }

    public Board getBoard(RoomId roomId) {
        return boardDao.getBoard(roomId);
    }

    @Override
    public String toString() {
        return "ChessService{" +
                "gameDao=" + gameRoomDao +
                ", boardDao=" + boardDao +
                '}';
    }
}
