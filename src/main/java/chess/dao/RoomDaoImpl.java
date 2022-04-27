package chess.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RoomDaoImpl implements RoomDao {

    private static final String DEFAULT_GAME_STATE = "ready";
    private static final String FIRST_TURN = "WHITE";

    private final JdbcTemplate jdbcTemplate;

    public RoomDaoImpl(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void saveNewRoom(final String roomName, final String password) {
        final String sql = "insert into room (name, password, gameState, turn) values (?, ?, ?, ?)";
        jdbcTemplate.update(sql, roomName, password, DEFAULT_GAME_STATE, FIRST_TURN);
    }

    @Override
    public boolean hasDuplicatedName(final String roomName) {
        final String sql = "select count(*) from room where name = ?";
        final int count = jdbcTemplate.queryForObject(sql, Integer.class, roomName);
        return count > 0;
    }

    @Override
    public String getPasswordByName(final int roomId) {
        final String sql = "select password from room where roomId = ?";
        return jdbcTemplate.queryForObject(sql, String.class, roomId);
    }

    @Override
    public String getGameStateByName(final int roomId) {
        final String sql = "select gameState from room where roomId = ?";
        return jdbcTemplate.queryForObject(sql, String.class, roomId);
    }

    @Override
    public void deleteRoomByName(final int roomId) {
        final String sql = "delete from room where roomId = ?";
        jdbcTemplate.update(sql, roomId);
    }

    public void saveTurn(final int roomId, final String turn) {
        final String sql = "update room set turn = ? where roomId = ?";
        jdbcTemplate.update(sql, turn, roomId);
    }

    @Override
    public String getTurn(final int roomId) {
        final String sql = "select turn from room where roomId = ?";
        return jdbcTemplate.queryForObject(sql, String.class, roomId);
    }

    @Override
    public void saveGameState(final int roomId, final String state) {
        final String sql = "update room set gameState = ? where roomId = ?";
        jdbcTemplate.update(sql, state, roomId);
    }
}
