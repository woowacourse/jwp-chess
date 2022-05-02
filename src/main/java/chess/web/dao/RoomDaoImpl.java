package chess.web.dao;

import chess.domain.game.state.Player;
import chess.domain.piece.property.Color;
import chess.web.dto.CreateRoomRequestDto;
import chess.web.dto.DeleteDto;
import chess.web.dto.DeleteResultDto;
import chess.web.dto.FinishResultDto;
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
    public FinishResultDto finish(int roomId) {
        final String sql = "update room set finished = 1 where id = (?)";
        this.jdbcTemplate.update(
                sql, roomId);
        return new FinishResultDto(roomId, true);
    }

    @Override
    public RoomDto isStartable(int roomId) {
        final String sql = "select id, title, finished from room where id = ?";
        return jdbcTemplate.queryForObject(sql, roomDtoRowMapper, roomId);
    }

    @Override
    public DeleteResultDto delete(int roomId, DeleteDto deleteDto) {
        final String sql = "update room set deleted = 1 where id = (?) AND password = (?)";
        int update = this.jdbcTemplate.update(
                sql, roomId, deleteDto.getPassword());
        if (update == 1) {
            return new DeleteResultDto(roomId, true);
        }
        return new DeleteResultDto(roomId, false);
    }

    @Override
    public Player getPlayer(int roomId) {
        final String sql = "select color from room where id = ?";
        String color = jdbcTemplate.queryForObject(sql, String.class, roomId);
        return Player.of(Color.of(color));
    }
}
