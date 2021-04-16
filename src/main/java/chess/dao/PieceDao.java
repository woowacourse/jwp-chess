package chess.dao;

import chess.domain.piece.Piece;
import chess.domain.piece.PieceFactory;
import chess.domain.piece.Position;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class PieceDao {

    private final JdbcTemplate jdbcTemplate;

    public PieceDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Piece> pieceRowMapper = (resultSet, rowNum) -> {
        int x = resultSet.getInt("x");
        int y = resultSet.getInt("y");
        String color = resultSet.getString("color");
        String shape = resultSet.getString("shape");

        return PieceFactory.createPiece(color, shape, new Position(y, x));

    };

    public void insertAll(long gameId, List<Piece> pieces) {
        String sql = "INSERT INTO piece(game_id, x, y, color, shape) VALUES(?,?,?,?,?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Piece piece = pieces.get(i);
                ps.setLong(1, gameId);
                ps.setInt(2, piece.getColumn());
                ps.setInt(3, piece.getRow());
                ps.setString(4, piece.getColorValue());
                ps.setString(5, piece.getShapeValue());
            }

            @Override
            public int getBatchSize() {
                return pieces.size();
            }
        });
    }

    public List<Piece> selectAll(long gameId) {
        String sql = "SELECT * FROM piece WHERE game_id = ?";
        return jdbcTemplate.query(sql, pieceRowMapper, gameId);
    }

    public void delete(long gameId, Position target) {
        String sql = "DELETE FROM piece WHERE game_id = ? AND x = ? AND y = ?";
        jdbcTemplate.update(sql, gameId, target.getColumn(), target.getRow());
    }

    public void update(long gameId, Position source, Position target) {
        String sql = "UPDATE piece SET x = ?, y = ? WHERE game_id = ? AND x = ? AND y = ?";
        jdbcTemplate.update(sql, target.getColumn(), target.getRow(), gameId, source.getColumn(), source.getRow());
    }

}
