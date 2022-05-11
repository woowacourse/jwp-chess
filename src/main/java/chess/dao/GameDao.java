package chess.dao;

import chess.domain.Camp;
import chess.entity.GameEntity;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class GameDao {
    private final JdbcTemplate jdbcTemplate;

    public GameDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<GameEntity> actorRowMapper = (resultSet, rowNum) -> new GameEntity(
            resultSet.getLong("id"),
            resultSet.getString("title"),
            resultSet.getString("password"),
            resultSet.getBoolean("white_turn"),
            resultSet.getBoolean("finished")
    );

    public Long save(GameEntity gameEntity) {
        final String sql = "insert into game (title, password, finished, white_turn) values (?, ?, false, true)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, gameEntity.getTitle());
            ps.setString(2, gameEntity.getPassword());
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public List<GameEntity> findAll() {
        final String sql = "select id, title, password, white_turn, finished from game";

        return jdbcTemplate.query(sql, actorRowMapper);
    }

    public GameEntity findById(Long id) {
        final String sql = "select id, title, password, white_turn, finished from game where id = ?";

        return jdbcTemplate.queryForObject(sql, actorRowMapper, id);
    }

    public void updateTurnById(Long id) {
        final String sql = "update game set white_turn = ? where id = ?";

        jdbcTemplate.update(sql, Camp.BLACK.isNotTurn(), id);
    }

    public void updateStateById(Long id) {
        final String sql = "update game set finished = true where id = ?";

        jdbcTemplate.update(sql, id);
    }

    public void deleteById(Long id) {
        final String sql = "delete from game where id = ?";

        jdbcTemplate.update(sql, id);
    }

    public boolean isWhiteTurn(Long id) {
        final String sql = "select white_turn from game where id = ?";

        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, id));
    }
}
