package chess.dao;

import chess.entity.Square;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SquareImplDao implements SquareDao {

    public final JdbcTemplate jdbcTemplate;

    public SquareImplDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int[] insertSquareAll(Long roomId, List<Square> squares) {
        String sql = "INSERT INTO square (room_id, position, symbol, color) VALUES (?, ?, ?, ?)";

        return jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Square square = squares.get(i);
                ps.setLong(1, square.getRoomId());
                ps.setString(2, square.getPosition());
                ps.setString(3, square.getSymbol());
                ps.setString(4, square.getColor());
            }

            @Override
            public int getBatchSize() {
                return squares.size();
            }
        });
    }

    @Override
    public List<Square> findSquareAllById(Long id) {
        String sql = "SELECT * FROM square WHERE room_id = (?)";

        return jdbcTemplate.query(sql, (rs, ronNum) -> new Square(
                rs.getLong("room_id"),
                rs.getString("position"),
                rs.getString("symbol"),
                rs.getString("color")
        ), id);
    }

    @Override
    public Long updateSquare(Square square) {
        String sql = "UPDATE square SET position = (?), symbol = (?), color = (?) WHERE room_id = (?) AND position = (?)";
        jdbcTemplate.update(sql,
                square.getPosition(), square.getSymbol(), square.getColor(), square.getRoomId(), square.getPosition());

        return square.getRoomId();
    }
}
