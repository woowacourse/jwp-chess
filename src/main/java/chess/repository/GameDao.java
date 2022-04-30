package chess.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;

@Repository
public class GameDao {

    private final JdbcTemplate jdbcTemplate;

    public GameDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean isDuplicateName(String name) {
        final String sql = "SELECT count(*) FROM game WHERE name=?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, name);
        return count > 0;
    }

    public int saveGame(String name, String password) {
        final String sql = "INSERT INTO game(name,password) VALUES (?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, name);
            ps.setString(2, password);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }
}
