package wooteco.chess.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import wooteco.chess.domain.Board;
import wooteco.chess.domain.Pieces;
import wooteco.chess.domain.Position;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.PieceRule;
import wooteco.chess.exception.DataAccessException;

public class BoardDao {
    private static final int FIRST_INDEX = 0;

    private JdbcTemplate template = new JdbcTemplate();

    public Pieces findByRoomId(int roomId) {
        try {
            RowMapper rm = rs -> {
                Map<Position, Piece> positionPairs = new HashMap<>();
                rs.beforeFirst();
                while (rs.next()) {
                    String type = rs.getString("type");
                    String position = rs.getString("position");
                    String team = rs.getString("team");
                    positionPairs.put(new Position(position),
                        PieceRule.makeNewPiece(type.charAt(FIRST_INDEX), position, team));
                }
                return new Pieces(positionPairs);
            };
            PreparedStatementSetter pss = statement -> statement.setInt(1, roomId);
            final String sql = "SELECT * FROM piece where room_id = ?";
            return (Pieces)template.executeQueryWithPss(sql, pss, rm);
        } catch (SQLException e) {
            throw new DataAccessException();
        }
    }

    public void editPiece(String position, String newPosition, int roomId) {
        try {
            PreparedStatementSetter pss = statement -> {
                statement.setString(1, newPosition);
                statement.setString(2, position);
                statement.setInt(3, roomId);
            };
            final String query = "UPDATE piece SET position = ? WHERE position = ? AND room_id = ?";
            template.executeUpdate(query, pss);
        } catch (SQLException e) {
            throw new DataAccessException();
        }
    }

    public void removeAll(int roomId) {
        try {
            PreparedStatementSetter pss = statement -> statement.setInt(1, roomId);
            final String sql = "DELETE FROM piece WHERE room_id =?";
            template.executeUpdate(sql, pss);
        } catch (SQLException e) {
            throw new DataAccessException();
        }
    }

    public void removePiece(String position, int roomId) {
        try {
            PreparedStatementSetter pss = statement -> {
                statement.setString(1, position);
                statement.setInt(2, roomId);
            };
            String query = "DELETE FROM piece WHERE position = ? AND room_id = ?";
            template.executeUpdate(query, pss);
        } catch (SQLException e) {
            throw new DataAccessException();
        }
    }

    public void saveBoard(Board board, int roomId) {
        try {
            PreparedStatementSetter pss = statement -> {
                for (Piece alivePiece : board.getPieces().getAlivePieces()) {
                    statement.setString(1, alivePiece.getPosition());
                    statement.setString(2, alivePiece.toString());
                    statement.setString(3, alivePiece.getTeam().getName());
                    statement.setInt(4, roomId);
                    statement.addBatch();
                }
            };
            final String sql = "INSERT INTO piece(position, type, team, room_id) VALUES (?, ?, ?, ?)";
            template.executeBatchWithPss(sql, pss);
        } catch (SQLException e) {
            throw new DataAccessException();
        }
    }
}