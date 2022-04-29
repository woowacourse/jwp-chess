package chess.dao;

import chess.entity.RoomEntity;
import java.util.HashMap;
import java.util.List;
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

    public int save(final RoomEntity roomEntity) {
        final Map<String, Object> parameters = fillParameters(roomEntity);
        final Number id = insertActor.executeAndReturnKey(parameters);

        return id.intValue();
    }

    private Map<String, Object> fillParameters(final RoomEntity roomEntity) {
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", roomEntity.getName());
        parameters.put("password", roomEntity.getPassword());
        parameters.put("game_status", roomEntity.getGameStatus());
        parameters.put("current_turn", roomEntity.getCurrentTurn());

        return parameters;
    }

    public Optional<RoomEntity> findById(final int id) {
        final String sql = "SELECT * FROM room WHERE id = ?";

        if (existsById(id)) {
            final RoomEntity room = jdbcTemplate.queryForObject(sql, createRoomRowMapper(), id);
            return Optional.of(room);
        }

        return Optional.empty();
    }

    public boolean existsById(final int id) {
        final String sql = "SELECT COUNT(*) from room where id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, id) == 1;
    }

    public List<RoomEntity> findAll() {
        final String sql = "SELECT * FROM room";
        return jdbcTemplate.query(sql, createRoomRowMapper());
    }

    private RowMapper<RoomEntity> createRoomRowMapper() {
        return (rs, rowNum) -> {
            final String roomId = rs.getString("id");
            final String name = rs.getString("name");
            final String password = rs.getString("password");
            final String gameStatus = rs.getString("game_status");
            final String currentTurn = rs.getString("current_turn");

            return new RoomEntity(Integer.parseInt(roomId), name, password, gameStatus, currentTurn);
        };
    }

    public void update(final RoomEntity roomEntity) {
        final String sql = "UPDATE room SET game_status = ?, current_turn = ? WHERE id = ?";
        jdbcTemplate.update(sql, roomEntity.getGameStatus(), roomEntity.getCurrentTurn(), roomEntity.getId());
    }

    public void deleteByIdAndPassword(final int id, final String password) {
        final String sql = "DELETE FROM room WHERE id = ? AND password = ?";
        jdbcTemplate.update(sql, id, password);
    }

    public void deleteAll() {
        final String sql = "DELETE FROM room";
        jdbcTemplate.update(sql);
    }
}
