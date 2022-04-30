package chess.dao;

import chess.domain.player.Team;
import chess.dto.GameNameAndTurnDto;
import chess.dto.RoomInfoDto;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class RoomDao {

    private JdbcTemplate jdbcTemplate;

    public RoomDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insert(final String roomName, final String password, final Team turn) {
        final String SQL = "insert into room (name, password, turn) values (?, ?, ?)";
        jdbcTemplate.update(SQL, roomName, password, turn.getName());
    }

    public Optional<Integer> findRoomIdByName(final String roomName) {
        final String SQL = "select id from room where name = (?)";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(SQL, Integer.class, roomName));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<Object> findRoomById(long roomId) {
        final String SQL = "select name from room where id = (?)";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(SQL, Integer.class, roomId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public String findTurn(final long roomId) {
        final String SQL = "select turn from room where id = (?)";
        return jdbcTemplate.queryForObject(SQL, String.class, roomId);
    }

    public GameNameAndTurnDto findNameAndTurnById(final long roomId) {
        final String SQL = "select name, turn from room where id =(?)";
        return jdbcTemplate.queryForObject(SQL,
                (resultSet, count) -> new GameNameAndTurnDto(
                        resultSet.getString("name"),
                        resultSet.getString("turn")
                ), roomId);
    }

    public String findRoomPasswordById(final long roomId) {
        final String SQL = "select password from room where id = (?)";
        return jdbcTemplate.queryForObject(SQL, String.class, roomId);
    }

    public List<RoomInfoDto> findAll() {
        final String SQL = "select id, name from room";
        return jdbcTemplate.query(SQL,
                (resultSet, count) -> new RoomInfoDto(
                        resultSet.getLong("id"),
                        resultSet.getString("name")
                ));
    }

    public void updateTurn(final long roomId, final Team turn) {
        final String SQL = "update room set turn = (?) where id = (?)";
        jdbcTemplate.update(SQL, turn.getName(), roomId);
    }

    public void delete(final long roomId) {
        final String SQL = "delete from room where id = (?)";
        jdbcTemplate.update(SQL, roomId);
    }
}
