package chess.dao.jdbctemplate;

import chess.dao.BoardDao;
import chess.dto.BoardDto;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcTemplateBoardDao implements BoardDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateBoardDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void update(String position, String piece) {
        String sql = "update board set piece = ? where position = ?";
        jdbcTemplate.update(sql, piece, position);
    }

    @Override
    public List<BoardDto> getBoard() {
        final String sql = "select * from board";
        return jdbcTemplate.query(sql, new BoardMapper());
    }

    @Override
    public void reset(Map<String, String> board) {
        removeAll();
        final String sql = "insert into board (position, piece) values (?,?)";
        for (Entry<String, String> boardEntry : board.entrySet()) {
            jdbcTemplate.update(sql, boardEntry.getKey(), boardEntry.getValue());
        }
    }

    private void removeAll() {
        String sql = "truncate table board";
        jdbcTemplate.update(sql);
    }
}
