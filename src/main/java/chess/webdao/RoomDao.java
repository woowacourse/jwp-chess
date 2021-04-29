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
    private JdbcTemplate jdbcTemplate;

    public RoomDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private RowMapper<TurnDto> turnMapper = (resultSet, rowNum) -> {
        TurnDto turnDto = new TurnDto();

        turnDto.setTurn(resultSet.getString("turn"));
        turnDto.setIsPlaying(resultSet.getBoolean("is_playing"));

        return turnDto;
    };

    private RowMapper<RoomDto> roomRowMapper = (resultSet, rowNum) -> {
        RoomDto roomDto = new RoomDto();

        roomDto.setRoomId(resultSet.getLong("room_id"));
        roomDto.setTurn(resultSet.getString("turn"));
        roomDto.setIsPlaying(resultSet.getBoolean("is_playing"));
        roomDto.setName(resultSet.getString("name"));

        return roomDto;
    };

    public void deleteRoomByRoomId(long roomId) {
        final String sql = "DELETE FROM room WHERE room_id = (?)";
        this.jdbcTemplate.update(sql, roomId);
    }

    public void changeTurnByRoomId(String turn, boolean isPlaying, long roomId) {
        final String sql = "UPDATE room SET turn = (?), is_playing = (?) WHERE room_id = (?)";
        this.jdbcTemplate.update(sql, turn, isPlaying, roomId);
    }

    public TurnDto selectTurnByRoomId(long roomId) {
        final String sql = "SELECT * FROM room WHERE room_id = (?)";
        return this.jdbcTemplate.queryForObject(sql, turnMapper, roomId);
    }

    public List<RoomDto> selectAllRooms() {
        final String sql = "SELECT * FROM room";
        return this.jdbcTemplate.query(sql, roomRowMapper);
    }

    public long createRoom(String currentTurn, boolean isPlaying, String roomName) {
        String sql = "INSERT INTO room (turn, is_playing, name) VALUES (?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        this.jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"room_id"});
            ps.setString(1, currentTurn);
            ps.setBoolean(2, isPlaying);
            ps.setString(3, roomName);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }
}
