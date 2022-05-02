package chess.database.dao;

import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import chess.database.entity.GameEntity;

@Repository
public class JdbcGameDao implements GameDao {

    public static final RowMapper<GameEntity> GAME_ENTITY_ROW_MAPPER = (resultSet, rowNum) -> new GameEntity(
        resultSet.getLong("id"),
        resultSet.getString("turn_color"),
        resultSet.getString("state"),
        resultSet.getLong("room_id")
    );
    private final SimpleJdbcInsert jdbcInsert;
    private final JdbcTemplate jdbcTemplate;

    public JdbcGameDao(DataSource dataSource, JdbcTemplate jdbcTemplate) {
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
            .withTableName("game")
            .usingGeneratedKeyColumns("id");
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long saveGame(GameEntity entity) {
        return jdbcInsert.executeAndReturnKey(new BeanPropertySqlParameterSource(entity)).longValue();
    }

    @Override
    public void updateGame(GameEntity entity) {
        final String sql = "UPDATE game SET state = ?, turn_color = ? WHERE id = ?";
        jdbcTemplate.update(sql, entity.getState(), entity.getTurnColor(), entity.getId());
    }

    @Override
    public Optional<GameEntity> findGameById(Long id) {
        final String sql = "SELECT * FROM game WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, GAME_ENTITY_ROW_MAPPER, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<GameEntity> findGameByRoomId(Long roomId) {
        final String sql = "SELECT * FROM game WHERE room_id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, GAME_ENTITY_ROW_MAPPER, roomId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
