package chess.dao.jdbctemplate;

import chess.dao.GameStatusDao;
import chess.domain.GameStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcTemplateGameStatusDao implements GameStatusDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateGameStatusDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void create(GameStatus data, int roomId) {
        String sql = "insert into game_status (status,roomId) values (?,?)";
        jdbcTemplate.update(sql, data.toString(), roomId);
    }

    @Override
    public void update(String nowStatus, String nextStatus, int roomId) {
        String sql = "update game_status set status = ? where status = ? and roomId = ?";
        jdbcTemplate.update(sql, nextStatus, nowStatus, roomId);
    }

    @Override
    public String getStatus(int roomId) {
        final String sql = "select status from game_status where roomId = ?";
        return jdbcTemplate.queryForObject(sql, String.class, roomId);
    }

    @Override
    public void reset(GameStatus data, int roomId) {
        removeAll(roomId);
        String sql = "insert into game_status (status,roomId) values (?,?)";
        jdbcTemplate.update(sql, data.toString(), roomId);
    }

    private void removeAll(int id) {
        String sql = "delete from game_status where roomId = ?";
        jdbcTemplate.update(sql, id);
    }
}
