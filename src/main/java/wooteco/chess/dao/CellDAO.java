package chess.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import chess.domain.board.Board;
import chess.domain.board.Position;
import chess.domain.piece.GamePiece;
import chess.domain.piece.GamePieces;
import chess.util.DBConnector;

public class CellDAO {

    private DBConnector dbConnector;

    public CellDAO(DBConnector dbConnector) {
        this.dbConnector = dbConnector;
    }

    public void addCells(Board board, int gameInfoId) throws SQLException {
        for (Map.Entry<Position, GamePiece> entry : board.getBoard().entrySet()) {
            dbConnector.executeUpdate("INSERT INTO board (gameinfo_id, position, piece) VALUES (?, ?, ?)",
                    String.valueOf(gameInfoId), entry.getKey().getName(), entry.getValue().getName());
        }
    }

    public Map<Position, GamePiece> findCellsByBoardId(int gameInfoId) throws SQLException {
        ResultSet rs = dbConnector.executeQuery("SELECT * FROM board WHERE gameinfo_id = ?", String.valueOf(gameInfoId));
        Map<Position, GamePiece> board = new HashMap<>();

        while (rs.next()) {
            Position position = Position.from(rs.getString("position"));
            GamePiece gamePiece = GamePieces.from(rs.getString("piece"));
            board.put(position, gamePiece);
        }

        return board;
    }

    public Map<Position, GamePiece> updateCellsByBoardId(Board board, int gameInfoId) throws SQLException {
        for (Map.Entry<Position, GamePiece> entry : board.getBoard().entrySet()) {
            dbConnector.executeUpdate("UPDATE board SET piece = ? WHERE position = ? AND gameinfo_id = ?",
                    entry.getValue().getName(), entry.getKey().getName(), String.valueOf(gameInfoId));
        }

        return findCellsByBoardId(gameInfoId);
    }

    public boolean deleteCellsByUser(int gameInfoId) throws SQLException {
        dbConnector.executeUpdate("DELETE FROM board WHERE gameinfo_id = ?", String.valueOf(gameInfoId));

        return findCellsByBoardId(gameInfoId) == null;
    }
}
