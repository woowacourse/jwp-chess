package wooteco.chess.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.piece.GamePiece;
import wooteco.chess.domain.piece.GamePieces;
import wooteco.chess.util.DBConnector;

public class BoardDAO {

    private DBConnector dbConnector;

    public BoardDAO(DBConnector dbConnector) {
        this.dbConnector = dbConnector;
    }

    public void addBoard(Board board, int gameInfoId) throws SQLException {
        for (Map.Entry<Position, GamePiece> entry : board.getBoard().entrySet()) {
            dbConnector.executeUpdate("INSERT INTO board (gameinfo_id, position, piece) VALUES (?, ?, ?)",
                    String.valueOf(gameInfoId), entry.getKey().getName(), entry.getValue().getName());
        }
    }

    public Map<Position, GamePiece> findBoardByGameInfoId(int gameInfoId) throws SQLException {
        ResultSet rs = dbConnector.executeQuery("SELECT * FROM board WHERE gameinfo_id = ?", String.valueOf(gameInfoId));
        Map<Position, GamePiece> board = new HashMap<>();

        while (rs.next()) {
            Position position = Position.from(rs.getString("position"));
            GamePiece gamePiece = GamePieces.from(rs.getString("piece"));
            board.put(position, gamePiece);
        }

        return board;
    }

    public Map<Position, GamePiece> updateBoardByGameInfoId(Board board, int gameInfoId) throws SQLException {
        for (Map.Entry<Position, GamePiece> entry : board.getBoard().entrySet()) {
            dbConnector.executeUpdate("UPDATE board SET piece = ? WHERE position = ? AND gameinfo_id = ?",
                    entry.getValue().getName(), entry.getKey().getName(), String.valueOf(gameInfoId));
        }

        return findBoardByGameInfoId(gameInfoId);
    }

    public boolean deleteBoardByGameInfoId(int gameInfoId) throws SQLException {
        dbConnector.executeUpdate("DELETE FROM board WHERE gameinfo_id = ?", String.valueOf(gameInfoId));

        return findBoardByGameInfoId(gameInfoId) == null;
    }
}
