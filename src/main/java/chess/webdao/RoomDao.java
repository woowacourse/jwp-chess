package chess.webdao;

import chess.webdto.dao.RoomDto;
import chess.webdto.dao.TurnDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class RoomDao {
    private static final String DELETE_ROOM_BY_ROOM_ID = "DELETE FROM room WHERE room_id = (?)";
    private static final String UPDATE_ROOM_BY_ROOM_ID = "UPDATE room SET turn = (?), is_playing = (?) WHERE room_id = (?)";
    private static final String SELECT_ROOM_BY_ROOM_ID = "SELECT * FROM room WHERE room_id = (?)";
    private static final String SELECT_ALL_ROOMS = "SELECT * FROM room";
    private static final String INSERT_ROOM = "INSERT INTO room (turn, is_playing, name, password) VALUES (?, ?, ?, ?)";
    private static final String INSERT_ROOM_WITHOUT_PASSWORD = "INSERT INTO room (turn, is_playing, name) VALUES (?, ?, ?)";
    private static final String CHECK_ROOM_ID_PASSWORD = "SELECT count(room_id) FROM room WHERE room_id = ? AND password = ?";

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<TurnDto> turnMapper = (resultSet, rowNum) -> {
        TurnDto turnDto = new TurnDto();

        turnDto.setTurn(resultSet.getString("turn"));
        turnDto.setIsPlaying(resultSet.getBoolean("is_playing"));

        return turnDto;
    };
    private final RowMapper<RoomDto> roomRowMapper = (resultSet, rowNum) -> {
        RoomDto roomDto = new RoomDto();

        roomDto.setRoomId(resultSet.getLong("room_id"));
        roomDto.setTurn(resultSet.getString("turn"));
        roomDto.setIsPlaying(resultSet.getBoolean("is_playing"));
        roomDto.setName(resultSet.getString("name"));

        return roomDto;
    };

    public RoomDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void deleteRoomByRoomId(long roomId) {
        this.jdbcTemplate.update(DELETE_ROOM_BY_ROOM_ID, roomId);
    }

    public void changeTurnByRoomId(String turn, boolean isPlaying, long roomId) {
        this.jdbcTemplate.update(UPDATE_ROOM_BY_ROOM_ID, turn, isPlaying, roomId);
    }

    public TurnDto selectTurnByRoomId(long roomId) {
        return this.jdbcTemplate.queryForObject(SELECT_ROOM_BY_ROOM_ID, turnMapper, roomId);
    }

    public List<RoomDto> selectAllRooms() {
        return this.jdbcTemplate.query(SELECT_ALL_ROOMS, roomRowMapper);
    }

    public long createRoom(String currentTurn, boolean isPlaying, String roomName, String password) {

        KeyHolder keyHolder = new GeneratedKeyHolder();
        this.jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(INSERT_ROOM, new String[]{"room_id"});
            ps.setString(1, currentTurn);
            ps.setBoolean(2, isPlaying);
            ps.setString(3, roomName);
            ps.setString(4, password);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public long createRoom(String currentTurn, boolean isPlaying, String roomName) {

        KeyHolder keyHolder = new GeneratedKeyHolder();
        this.jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(INSERT_ROOM_WITHOUT_PASSWORD, new String[]{"room_id"});
            ps.setString(1, currentTurn);
            ps.setBoolean(2, isPlaying);
            ps.setString(3, roomName);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public int checkPassword(long roomId, String password) {
        return this.jdbcTemplate.queryForObject(CHECK_ROOM_ID_PASSWORD, int.class, roomId, password);
    }
}
