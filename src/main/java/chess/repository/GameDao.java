package chess.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.LinkedHashMap;
import java.util.Map;

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

    public long saveGame(String name, String password) {
        final String sql = "INSERT INTO game(name,password) VALUES (?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, name);
            ps.setString(2, password);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public Map<Long, String> findGameList() {
        final String sql = "SELECT id,name FROM game ORDER BY id ASC";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);

        Map<Long, String> gameList = new LinkedHashMap<>();
        while (rowSet.next()) {
            gameList.put(rowSet.getLong("id"), rowSet.getString("name"));
        }

        return gameList;
    }

    public String findPasswordById(long gameId) {
        final String sql = "SELECT password FROM game WHERE id=?";
        return jdbcTemplate.queryForObject(sql, String.class, gameId);
    }

    public void delete(long gameId) {
        final String sql = "DELETE FROM game WHERE id=?";
        jdbcTemplate.update(sql, gameId);
    }
}
