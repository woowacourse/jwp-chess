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
    public String getPasswordByName(final String roomName) {
        final String sql = "select password from room where name = ?";
        return jdbcTemplate.queryForObject(sql, String.class, roomName);
    }

    @Override
    public String getGameStateByName(final String roomName) {
        final String sql = "select gameState from room where name = ?";
        return jdbcTemplate.queryForObject(sql, String.class, roomName);
    }

    @Override
    public void deleteRoomByName(final String roomName) {
        final String sql = "delete from room where name = ?";
        jdbcTemplate.update(sql, roomName);
    }

    public void saveTurn(final String roomName, final String turn) {
        final String sql = "update room set turn = ? where name = ?";
        jdbcTemplate.update(sql, turn, roomName);
    }

    @Override
    public String getTurn(final String roomName) {
        final String sql = "select turn from room where name = ?";
        return jdbcTemplate.queryForObject(sql, String.class, roomName);
    }

    @Override
    public void saveGameState(final String roomName, final String state) {
        final String sql = "update room set gameState = ? where name = ?";
        jdbcTemplate.update(sql, state, roomName);
    }

    @Override
    public int getRoomId(final String roomName) {
        final String sql = "select roomIndex from room where name = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, roomName);
    }
}
