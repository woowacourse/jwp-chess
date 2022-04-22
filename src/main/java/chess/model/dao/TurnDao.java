package chess.model.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.util.Optional;

@Repository
public class TurnDao {
    private final JdbcTemplate jdbcTemplate;

    public TurnDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void init() {
        String query = "insert into turns (turn) values (?)";
        jdbcTemplate.update(query, "WHITE");
    }

    public Optional<String> findOne() {
        String query = "select turn from turns limit 1";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(query, String.class));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public void update(String nextTurn) {
        String query = "UPDATE turns SET turn = (?)";
        jdbcTemplate.update(query, nextTurn);
    }

    public void deleteAll() {
        String query = "DELETE FROM turns";
        jdbcTemplate.update(query);
    }
}
