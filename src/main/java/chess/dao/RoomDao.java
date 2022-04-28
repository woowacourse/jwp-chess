package chess.dao;

import chess.domain.player.Team;
import chess.dto.GameNameAndTurnDto;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class RoomDao {

    private JdbcTemplate jdbcTemplate;

    public RoomDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Integer> findRoomIdByName(final String roomName) {
        final String sql = "select id from room where name = (?)";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, Integer.class, roomName));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void save(final String roomName, final String password, final Team turn) {
        final String sql = "insert into room (name, password, turn) values (?, ?, ?)";
        jdbcTemplate.update(sql, roomName, password, turn.getName());
    }

    public String findTurn(final long roomId) {
        final String sql = "select turn from room where id = (?)";
        return jdbcTemplate.queryForObject(sql, String.class, roomId);
    }

    public GameNameAndTurnDto findNameAndTurnById(final long roomId) {
        final String sql = "select name, turn from room where id =(?)";
        return jdbcTemplate.queryForObject(sql,
                (resultSet, count) -> new GameNameAndTurnDto(
                        resultSet.getString("name"),
                        resultSet.getString("turn")
                ), roomId);
    }

    public void updateTurn(final long roomId, final Team turn) {
        final String sql = "update room set turn = (?) where id = (?)";
        jdbcTemplate.update(sql, turn.getName(), roomId);
    }

    public void delete(final long roomId) {
        final String sql = "delete from room where id = (?)";
        jdbcTemplate.update(sql, roomId);
    }
}
