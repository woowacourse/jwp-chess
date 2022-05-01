package chess.web.dao;

import chess.domain.state.StateType;
import chess.web.dto.game.TitleDto;
import java.sql.PreparedStatement;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class GameDaoJdbcImpl implements GameDao {

    private static final RowMapper<StateType> STATE_TYPE_ROW_MAPPER = (resultSet, rowNum) -> {
        return StateType.from(
                resultSet.getString("state")
        );
    };

    private static final RowMapper<TitleDto> GAME_DTO_ROW_MAPPER = (resultSet, rowNum) -> {
        return new TitleDto(
                resultSet.getInt("id"),
                resultSet.getString("title")
        );
    };

    private final JdbcTemplate jdbcTemplate;

    public GameDaoJdbcImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int save(String title, String password, StateType stateType) {
        final String sql = "insert into game (title, password, state) values (?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql, new String[]{"id"});
            statement.setString(1, title);
            statement.setString(2, password);
            statement.setString(3, stateType.getNotation());
            return statement;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    @Override
    public void updateStateById(int id, StateType stateType) {
        final String sql = "update game set state = ? where id = ?";
        jdbcTemplate.update(sql, stateType.getNotation(), id);
    }

    @Override
    public StateType findStateById(int id) {
        final String sql = "select state from game where id = ?";
        return jdbcTemplate.queryForObject(sql, STATE_TYPE_ROW_MAPPER, id);
    }

    @Override
    public String findPasswordById(int id) {
        final String sql = "select password from game where id = ?";
        return jdbcTemplate.queryForObject(sql, String.class, id);
    }

    @Override
    public List<TitleDto> findAll() {
        final String sql = "select id, title from game";
        return jdbcTemplate.query(sql, GAME_DTO_ROW_MAPPER);
    }

    @Override
    public void deleteGameById(int id) {
        final String sql = "delete from game where id = ?";
        jdbcTemplate.update(sql, id);
    }
}
