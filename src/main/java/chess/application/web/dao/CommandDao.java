package chess.application.web.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommandDao {
  
    private final JdbcTemplate jdbcTemplate;

    public CommandDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insert(final int id, final String command) {
        String sql = "insert into command (id, command) values (?, ?)";
        jdbcTemplate.update(sql, id, command);
    }

    public List<String> findAllById(final int id) {
        String sql = String.format("select command from command where id = %d", id);
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> {
            return resultSet.getString("command");
        });
    }

    public void clear() {
        String sql = "truncate table command";
        jdbcTemplate.update(sql);
    }
}
