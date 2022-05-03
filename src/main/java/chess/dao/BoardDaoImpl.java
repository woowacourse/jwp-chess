package chess.dao;

import chess.domain.piece.Piece;
import chess.domain.piece.Pieces;
import chess.domain.position.Position;
import java.util.HashMap;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BoardDaoImpl implements BoardDao {

    private final JdbcTemplate jdbcTemplate;

    public BoardDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Map<Position, Piece> findAll(Long roomId) {
        String sql = "select * from board where room_id = ?";
        Map<Position, Piece> pieces = new HashMap<>();
        jdbcTemplate.query(sql,
            (rs, rowNum) -> pieces.put(
                Position.from(rs.getString("position")),
                Pieces.find(rs.getString("symbol"))), roomId);
        return pieces;
    }

    @Override
    public void saveAll(Map<Position, Piece> board, Long roomId) {
        final String sql = "insert into board (position, symbol, room_id) values(?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, board.keySet(), board.size(),
            (rs, position) -> {
                rs.setString(1, position.getPositionToString());
                rs.setString(2, board.get(position).getSymbol());
                rs.setLong(3, roomId);
            });
    }

    @Override
    public void delete(Long roomId) {
        final String sql = "delete from board where room_id = ?";
        jdbcTemplate.update(sql, roomId);
    }

    @Override
    public void updatePosition(String symbol, String position, Long roomId) {
        final String sql = "update board set symbol = ? where position = ? and room_id = ?";
        jdbcTemplate.update(sql, symbol, position, roomId);
    }
}
