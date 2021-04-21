package chess.dao.jdbctemplate;

import chess.dao.RoomDao;
import chess.domain.piece.PieceColor;
import chess.dto.RoomNameDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoomDaoTemplate implements RoomDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<RoomNameDto> actorRowMapper = (resultSet, rowNum) ->
        new RoomNameDto(
            resultSet.getString("name")
        );

    public RoomDaoTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<RoomNameDto> findRoomNames() {
        String sql = "SELECT * FROM room";
        return jdbcTemplate.query(sql, actorRowMapper);
    }

    @Override
    public String findRoomTurnColor(String roomName) {
        String sql = "SELECT turn FROM room WHERE name = ?";
        return jdbcTemplate.queryForObject(sql, String.class, roomName);
    }

    @Override
    public void addRoom(String name, PieceColor turnColor) {
        String sql = "INSERT INTO room VALUES (?, ?)";
        jdbcTemplate.update(sql, name, turnColor.getName());
    }

    @Override
    public void deleteRoom(String roomName) {
        String sql = "DELETE FROM room WHERE name = ?";
        jdbcTemplate.update(sql, roomName);
    }

    @Override
    public void updateTurn(String roomName, PieceColor turnColor) {
        String sql = "UPDATE room SET turn = ? WHERE name = ?";
        jdbcTemplate.update(sql, turnColor.getName(), roomName);
    }

    @Override
    public boolean existsRoom(String name) {
        String sql = "SELECT * FROM room WHERE name = ?";
        return jdbcTemplate.query(sql, actorRowMapper, name)
            .stream()
            .findAny()
            .isPresent();
    }

}
