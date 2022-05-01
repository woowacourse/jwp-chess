package chess.dao;

import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.dto.response.PieceDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class BoardDaoImpl implements BoardDao {

    private JdbcTemplate jdbcTemplate;

    public BoardDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<PieceDto> findAll(Long roomId) {
        String sql = "select * from board where room_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new PieceDto(
                    rs.getString("position"),
                    rs.getString("symbol")
                ),
            roomId);
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
