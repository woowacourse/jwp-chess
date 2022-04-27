package chess.web.dao;

import chess.domain.game.state.Player;
import chess.domain.piece.property.Color;
import chess.web.dto.CreateRoomRequestDto;
import chess.web.dto.ReadRoomResultDto;
import chess.web.dto.RoomDto;
import java.sql.PreparedStatement;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class RoomDaoImpl implements RoomDao {

    private final JdbcTemplate jdbcTemplate;

    public RoomDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<RoomDto> roomDtoRowMapper = (resultSet, rowNum) -> {
        RoomDto roomDto = new RoomDto();
        roomDto.setRoomId(resultSet.getInt("id"));
        roomDto.setRoomTitle(resultSet.getString("title"));
        roomDto.setFinished(resultSet.getBoolean("finished"));
        return roomDto;
    };

    @Override
    public void saveTurn(Color color, int roomId) {
        final String sql = "update room set color = (?) where id = (?)";
        this.jdbcTemplate.update(
                sql,
                color.name(), roomId);
    }

    @Override
    public void deleteAll(int roomId) {
        final String sql = "delete from room where id = (?)";
        this.jdbcTemplate.update(sql, roomId);
    }

    @Override
    public int createRoom(CreateRoomRequestDto createRoomRequestDto) {
        final String sql = "insert into room (title, password) values (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[] { "id" });
            ps.setString(1, createRoomRequestDto.getRoomTitle());
            ps.setString(2, createRoomRequestDto.getRoomPassword());
            return ps;
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }

    @Override
    public ReadRoomResultDto findAll() {
        final String sql = "select id, title, finished from room";
        return new ReadRoomResultDto(jdbcTemplate.query(sql, roomDtoRowMapper));
    }

    @Override
    public void changeTurn(int roomId) {
        Player player = getPlayer(roomId);
        saveTurn(player.change().getColor(), roomId);
    }

    @Override
    public boolean roomExist(int roomId) {
        final String sql = "select count(*) from room where id = (?)";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, roomId);
        return count > 0;
    }

    @Override
    public void finish(int roomId) {
        final String sql = "update room set finished = 1 where id = (?)";
        this.jdbcTemplate.update(
                sql, roomId);
    }

    @Override
    public RoomDto isStartable(int roomId) {
        final String sql = "select id, title, finished from room where id = ?";
        return jdbcTemplate.queryForObject(sql, roomDtoRowMapper, roomId);
    }

    @Override
    public Player getPlayer(int roomId) {
        final String sql = "select color from room where id = ?";
        String color = jdbcTemplate.queryForObject(sql, String.class, roomId);
        return Player.of(Color.of(color));
    }
}
