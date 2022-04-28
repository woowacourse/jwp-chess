package chess.database.dao.spring;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import chess.database.dao.GameDao;
import chess.database.dto.GameStateDto;

@Repository
public class SpringGameDao implements GameDao {

    private final SimpleJdbcInsert jdbcInsert;
    private final JdbcTemplate jdbcTemplate;

    public SpringGameDao(DataSource dataSource, JdbcTemplate jdbcTemplate) {
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
            .withTableName("game")
            .usingGeneratedKeyColumns("id");
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<GameStateDto> findGameById(Long id) {
        String sql = "select * from game where id = ?";
        try {
            final GameStateDto gameStateDto = jdbcTemplate.queryForObject(sql,
                (resultSet, rowNum) -> new GameStateDto(
                    resultSet.getString("state"),
                    resultSet.getString("turn_color")),
                id);
            return Optional.ofNullable(gameStateDto);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<GameStateDto> findGameByRoomName(String roomName) {
        String sql = "select * from game where room_name = ?";
        try {
            final GameStateDto gameStateDto = jdbcTemplate.queryForObject(sql,
                (resultSet, rowNum) -> new GameStateDto(
                    resultSet.getString("state"),
                    resultSet.getString("turn_color")),
                roomName);
            return Optional.ofNullable(gameStateDto);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<String> findPasswordById(Long roomId) {
        String sql = "SELECT password FROM game WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, String.class, roomId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Long saveGame(GameStateDto gameStateDto, String roomName, String password) {
        Map<String, Object> parameters = Map.of(
            "room_name", roomName,
            "password", password,
            "turn_color", gameStateDto.getTurnColor(),
            "state", gameStateDto.getState()
        );
        final Number idNumber = jdbcInsert.executeAndReturnKey(parameters);
        return idNumber.longValue();
    }

    @Override
    public void updateState(GameStateDto gameStateDto, Long id) {
        final String sql = "UPDATE game SET state = ?, turn_color = ? WHERE id = ?";
        jdbcTemplate.update(sql, gameStateDto.getState(), gameStateDto.getTurnColor(), id);
    }

    @Override
    public void removeGame(Long id) {
        final String sql = "DELETE FROM game WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Map<Long, String> readGameRoomIdAndNames() {
        final String sql = "SELECT id, room_name FROM game";
        return jdbcTemplate.query(sql, (rs, rowNum) -> Map.entry(
                rs.getLong("id"),
                rs.getString("room_name"))
            ).stream()
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
