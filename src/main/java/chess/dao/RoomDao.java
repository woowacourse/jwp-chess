package chess.dao;

import chess.domain.GameStatus;
import chess.domain.chesspiece.Color;
import chess.dto.CurrentTurnDto;
import chess.dto.RoomNameDto;
import chess.dto.RoomStatusDto;
import chess.entity.RoomEntity;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class RoomDao {

    private static final int IS_EXIST = 1;

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertActor;

    public RoomDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertActor = new SimpleJdbcInsert(dataSource)
                .withTableName("rooms")
                .usingGeneratedKeyColumns("id");
    }

    public List<RoomNameDto> findAllRoomName() {
        final String sql = "SELECT name FROM rooms";
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> RoomNameDto.from(resultSet));
    }

    public List<RoomEntity> findAll() {
        final String sql = "SELECT * FROM rooms";
        return jdbcTemplate.query(sql, rowMapper());
    }

    private RowMapper<RoomEntity> rowMapper() {
        return (resultSet, rowNum) -> new RoomEntity(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("password"),
                resultSet.getString("status"),
                resultSet.getString("turn")
        );
    }

    public RoomEntity save(final RoomEntity roomEntity) {
        SqlParameterSource parameters = new BeanPropertySqlParameterSource(roomEntity);
        Long id = insertActor.executeAndReturnKey(parameters).longValue();
        return new RoomEntity(id, roomEntity.getName(), roomEntity.getPassword(), roomEntity.getStatus(),
                roomEntity.getTurn());
    }

    public RoomEntity findById(final Long id) {
        String sql = "SELECT * FROM rooms WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper(), id);
    }

    public void deleteById(final Long id) {
        final String sql = "DELETE FROM rooms WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public boolean isExistName(final String roomName) {
        final String sql = "SELECT EXISTS (SELECT name FROM room WHERE name = ?)";
        return jdbcTemplate.queryForObject(sql, Integer.class, roomName) == IS_EXIST;
    }

    public CurrentTurnDto findCurrentTurnByName(final String roomName) {
        final String sql = "SELECT name, current_turn FROM room WHERE name = ?";
        return jdbcTemplate.queryForObject(sql, (resultSet, rowNum) -> CurrentTurnDto.from(resultSet), roomName);
    }

    public RoomStatusDto findStatusByName(final String roomName) {
        final String sql = "SELECT name, game_status FROM room WHERE name = ?";
        return jdbcTemplate.queryForObject(sql, (resultSet, rowNum) -> RoomStatusDto.from(resultSet), roomName);
    }

    public int delete(final String roomName) {
        final String sql = "DELETE FROM room WHERE name = ?";
        return jdbcTemplate.update(sql, roomName);
    }

    public int update(final String roomName, final GameStatus gameStatus, final Color currentTurn) {
        final String sql = "UPDATE room SET game_status = ?, current_turn = ? WHERE name = ?";
        return jdbcTemplate.update(sql, gameStatus.getValue(), currentTurn.getValue(), roomName);
    }

    public void updateStatusById(final Long id, final String status) {
        final String sql = "UPDATE rooms SET status = ? WHERE id = ?";
        jdbcTemplate.update(sql, status, id);
    }

    public void updateTurnById(final Long id, final String turn) {
        final String sql = "UPDATE rooms SET turn = ? WHERE id = ?";
        jdbcTemplate.update(sql, turn, id);
    }

    public int updateStatusTo(final String roomName, final GameStatus gameStatus) {
        final String sql = "UPDATE room SET game_status = ? WHERE name = ?";
        return jdbcTemplate.update(sql, gameStatus.getValue(), roomName);
    }
}
