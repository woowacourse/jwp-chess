package chess.dao.jdbctemplate;

import chess.dao.TurnDao;
import chess.domain.piece.Team;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class JdbcTemplateTurnDao implements TurnDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateTurnDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void create(Team data, int roomId) {
        String sql = "insert into turn (team, roomId) values (?,?)";
        jdbcTemplate.update(sql, data.toString(), roomId);
    }

    @Override
    public void update(String nowTurn, String nextTurn, int roomId) {
        String sql = "update turn set team = ? where team = ? and roomId = ?";
        jdbcTemplate.update(sql, nextTurn, nowTurn, roomId);
    }

    @Override
    public String getTurn(int roomId) {
        String sql = "select team from turn where roomId = ?";
        return jdbcTemplate.queryForObject(sql, String.class, roomId);
    }

    @Override
    public void reset(Team data, int roomId) {
        removeAll(roomId);
        String sql = "insert into turn (team, roomId) values (?,?)";
        jdbcTemplate.update(sql, data.toString(), roomId);
    }

    private void removeAll(int id) {
        String sql = "delete from turn where roomId = ?";
        jdbcTemplate.update(sql, id);
    }
}
