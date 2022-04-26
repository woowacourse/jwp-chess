package chess.web.dao;

import chess.domain.game.state.Player;
import chess.domain.piece.property.Color;
import chess.web.dto.CreateRoomRequestDto;
import java.sql.PreparedStatement;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class RoomDaoImpl implements RoomDao {

    private final JdbcTemplate jdbcTemplate;

    public RoomDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Color color) {
        final String sql = "insert into room (title, password, color) values (?, ?, ?) where id = 1";
        this.jdbcTemplate.update(
                sql,
                "testTitle", "testPassword", color.name());
    }

    @Override
    public void deleteAll() {
        final String sql = "delete from room";
        this.jdbcTemplate.update(sql);
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
    public Player getPlayer() {
        final String sql = "select color from room where id = `1`";
        String color = jdbcTemplate.queryForObject(sql, String.class);
        return Player.of(Color.of(color));
    }
}
