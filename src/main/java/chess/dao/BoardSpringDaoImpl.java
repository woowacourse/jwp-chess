package chess.dao;

import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.dto.PieceDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class BoardSpringDaoImpl implements BoardDao {

    private JdbcTemplate jdbcTemplate;

    public BoardSpringDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<PieceDto> findAll(long roomId) {
        String sql = "select * from board where room_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                        new PieceDto(
                                rs.getString("position"),
                                rs.getString("symbol")
                        ),
                roomId);
    }
    @Override
    public void saveAll(Map<Position, Piece> board, long roomId) {
        final String sql = "insert into board (position, symbol, room_id) values(?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, board.keySet(), board.size(),
                (rs, position) -> {
                    rs.setString(1, position.getPositionToString());
                    rs.setString(2, board.get(position).getSymbol());
                    rs.setLong(3, roomId);
                });
    }

    @Override
    public void delete(long roomId) {
        final String sql = "delete from board where room_id = ?";
        jdbcTemplate.update(sql, roomId);
    }

    @Override
    public void updatePosition(String source, String destination) {
        final String sql = "update board set symbol = ? where position = ?";
        jdbcTemplate.update(sql, source, destination);
    }
}
