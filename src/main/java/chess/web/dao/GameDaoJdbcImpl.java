package chess.web.dao;

import chess.domain.state.StateType;
import chess.web.dto.GameResponseDto;
import java.sql.PreparedStatement;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class GameDaoJdbcImpl implements GameDao {

    private static final RowMapper<StateType> stateTypeRowMapper = (resultSet, rowNum) -> {
        return StateType.from(
                resultSet.getString("state")
        );
    };

    private static final RowMapper<GameResponseDto> gameDtoRowMapper = (resultSet, rowNum) -> {
        return new GameResponseDto(
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
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, title);
            ps.setString(2, password);
            ps.setString(3, stateType.getNotation());
            return ps;
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
        return jdbcTemplate.queryForObject(sql, stateTypeRowMapper, id);
    }

    @Override
    public String findPasswordById(int id) {
        final String sql = "select password from game where id = ?";
        return jdbcTemplate.queryForObject(sql, String.class, id);
    }

    @Override
    public List<GameResponseDto> findAll() {
        final String sql = "select id, title from game";
        return jdbcTemplate.query(sql, gameDtoRowMapper);
    }

    @Override
    public void deleteGameById(int id) {
        final String sql = "delete from game where id = ?";
        jdbcTemplate.update(sql, id);
    }
}
