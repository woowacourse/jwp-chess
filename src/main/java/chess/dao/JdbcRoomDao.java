package chess.dao;

import chess.dao.dto.RoomSaveDto;
import chess.dao.dto.RoomUpdateDto;
import chess.entity.Room;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcRoomDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertActor;

    public JdbcRoomDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertActor = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("room")
                .usingGeneratedKeyColumns("id");
    }

    public int save(final RoomSaveDto saveDto) {
        final Map<String, Object> parameters = fillParameters(saveDto);
        final Number id = insertActor.executeAndReturnKey(parameters);

        return id.intValue();
    }

    private Map<String, Object> fillParameters(final RoomSaveDto saveDto) {
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", saveDto.getName());
        parameters.put("password", saveDto.getPassword());
        parameters.put("game_status", saveDto.getGameStatus());
        parameters.put("current_turn", saveDto.getCurrentTurn());

        return parameters;
    }

    public Optional<Room> findById(final int id) {
        final String sql = "SELECT * FROM room WHERE id = ?";

        if (existsById(id)) {
            Room room = jdbcTemplate.queryForObject(sql, createRoomRowMapper(), id);
            return Optional.of(room);
        }

        return Optional.empty();
    }

    private boolean existsById(final int id) {
        final String sql = "SELECT COUNT(*) from room where id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, id) == 1;
    }

    private RowMapper<Room> createRoomRowMapper() {
        return (rs, rowNum) -> {
            final String roomId = rs.getString("id");
            final String name = rs.getString("name");
            final String password = rs.getString("password");
            final String gameStatus = rs.getString("game_status");
            final String currentTurn = rs.getString("current_turn");

            return new Room(Integer.parseInt(roomId), name, password, gameStatus, currentTurn);
        };
    }

    public void update(final RoomUpdateDto updateDto) {
        final String sql = "UPDATE room SET game_status = ?, current_turn = ? WHERE id = ?";
        int rowCount = jdbcTemplate.update(sql, updateDto.getGameStatus(), updateDto.getCurrentTurn(), updateDto.getId());

        if (rowCount == 0) {
            throw new IllegalStateException("수정에 실패 하였습니다.");
        }
    }

    public void deleteById(final int id) {
        final String sql = "DELETE FROM room WHERE id = ?";
        int rowCount = jdbcTemplate.update(sql, id);

        if (rowCount == 0) {
            throw new IllegalStateException("삭제에 실패 하였습니다.");
        }
    }
}
