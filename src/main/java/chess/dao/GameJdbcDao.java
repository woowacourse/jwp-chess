package chess.dao;

import chess.dto.GameDto;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

@Repository
public class GameJdbcDao implements GameDao {

    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;

    public GameJdbcDao(JdbcTemplate jdbcTemplate, PasswordEncoder passwordEncoder) {
        this.jdbcTemplate = jdbcTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public int save(GameDto gameDto) {
        final String sql = "insert into game (room_name, password, state) values (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, gameDto.getRoomName());
            ps.setString(2, passwordEncoder.encode(gameDto.getPassword()));
            ps.setString(3, gameDto.getState());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    @Override
    public String findStateById(int gameId) {
        final String sql = "select state from game where id = (?)";
        return jdbcTemplate.queryForObject(sql, String.class, gameId);
    }

    @Override
    public void update(String state, int gameId) {
        final String sql = "update game set state = (?) where id = (?)";
        jdbcTemplate.update(sql, state, gameId);
    }

    @Override
    public void deleteById(int gameId) {
        final String sql = "delete from game where id = (?)";
        jdbcTemplate.update(sql, gameId);
    }

    @Override
    public String findPasswordById(int id) {
        final String sql = "select password from game where id = ?";
        return jdbcTemplate.queryForObject(sql, String.class, id);
    }

    @Override
    public List<GameDto> findGames() {
        final String sql = "select id, room_name, state from game";
        return jdbcTemplate.query(sql, (resultSet, rowNum) ->
                new GameDto(resultSet.getInt("id"),
                        resultSet.getString("room_name"),
                        resultSet.getString("state")));
    }
}
