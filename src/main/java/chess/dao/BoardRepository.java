package chess.dao;

import chess.entity.BoardEntity;
import java.util.Map;
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
    public Map<String, String> getBoard() {
        String sql = "select * from board";
        jdbcTemplate.query(sql, rowMapper());
        throw new UnsupportedOperationException("BoardRepository#getBoard not implemented.");
    }

    private RowMapper<BoardEntity> rowMapper() {
        return (rs, rowNum) -> {
            final String position = rs.getString("position");
            final String piece = rs.getString("piece");
            return new BoardEntity(position, piece);
        };
    }

    @Override
    public void updatePosition(final String position, final String piece) {
        throw new UnsupportedOperationException("BoardRepository#updatePosition not implemented.");
    }

    @Override
    public void updateBatchPositions(final Map<String, String> board) {
        throw new UnsupportedOperationException("BoardRepository#updateBatchPositions not implemented.");
    }
}
