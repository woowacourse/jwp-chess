package chess.web.dao;

import chess.domain.state.StateType;
import chess.web.dto.GameDto;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class GameDaoJdbcImpl implements GameDao {

    private static final RowMapper<StateType> stateTypeRowMapper = (resultSet, rowNum) -> {
        return StateType.from(
                resultSet.getString("state")
        );
    };

    private static final RowMapper<GameDto> gameDtoRowMapper = (resultSet, rowNum) -> {
        return new GameDto(
                resultSet.getInt("id"),
                resultSet.getString("title")
        );
    };

    private final JdbcTemplate jdbcTemplate;

    public GameDaoJdbcImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(String title, String password, StateType stateType) {
        final String sql = "insert into game (title, password, state) values (?, ?, ?)";
        jdbcTemplate.update(sql, title, password, stateType.getNotation());
    }

    @Override
    public void updateStateById(int id, StateType stateType) {
        final String sql = "update game set state = ? where id = ?";
        jdbcTemplate.update(sql, stateType.getNotation(), id);
    }

    @Override
    public StateType findStateById(int id) {
        final String sql = "select state from game where id = ?";
        return jdbcTemplate.queryForObject(sql, stateTypeRowMapper, id);
    }

    @Override
    public String findPasswordById(int id) {
        final String sql = "select password from game where id = ?";
        return jdbcTemplate.queryForObject(sql, String.class, id);
    }

    @Override
    public List<GameDto> findAll() {
        final String sql = "select id, title from game";
        return jdbcTemplate.query(sql, gameDtoRowMapper);
    }

    @Override
    public void deleteGameById(int id) {
        final String sql = "delete from game where id = ?";
        jdbcTemplate.update(sql, id);
    }
}
