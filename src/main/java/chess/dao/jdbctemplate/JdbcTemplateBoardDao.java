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
    public void create(Map<String, String> board, int roomId) {
        final String sql = "insert into board (position, piece, roomId) values (?,?,?)";
        for (Entry<String, String> boardEntry : board.entrySet()) {
            jdbcTemplate.update(sql, boardEntry.getKey(), boardEntry.getValue(), roomId);
        }
    }

    @Override
    public void update(String position, String piece, int roomId) {
        String sql = "update board set piece = ? where position = ? and roomId = ?";
        jdbcTemplate.update(sql, piece, position, roomId);
    }

    @Override
    public List<BoardDto> getBoard(int roomId) {
        final String sql = "select * from board where roomId = ?";
        return jdbcTemplate.query(sql, new BoardMapper(), roomId);
    }

    @Override
    public void reset(Map<String, String> board, int id) {
        removeAll(id);
        final String sql = "insert into board (position, piece, roomId) values (?,?,?)";
        for (Entry<String, String> boardEntry : board.entrySet()) {
            jdbcTemplate.update(sql, boardEntry.getKey(), boardEntry.getValue(), id);
        }
    }

    private void removeAll(int id) {
        String sql = "delete from board where roomId = ?";
        jdbcTemplate.update(sql, id);
    }
}
