package chess.dao;

import chess.entity.BoardEntity;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;


@Repository
public class BoardRepository implements BoardDao {

    private final JdbcTemplate jdbcTemplate;

    public BoardRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<BoardEntity> getBoard() {
        String sql = "select * from board";
        return jdbcTemplate.query(sql, rowMapper());
    }

    private RowMapper<BoardEntity> rowMapper() {
        return (rs, rowNum) -> {
            final String position = rs.getString("position");
            final String piece = rs.getString("piece");
            return new BoardEntity(position, piece);
        };
    }

    @Override
    public void updatePosition(final BoardEntity board) {
        final String sql = "update board set piece = ? where position =?";
        jdbcTemplate.update(sql, board.getPiece(), board.getPosition());
    }

    @Override
    public void updateBatchPositions(final List<BoardEntity> board) {
        throw new UnsupportedOperationException("BoardRepository#updateBatchPositions not implemented.");
    }
}
