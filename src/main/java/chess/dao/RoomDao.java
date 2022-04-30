package chess.dao;

import chess.domain.GameStatus;
import chess.domain.chesspiece.Color;
import chess.dto.response.CurrentTurnDto;
import chess.dto.response.RoomResponseDto;
import chess.dto.response.RoomStatusDto;
import chess.entity.RoomEntity;
import chess.exception.NotFoundException;
import java.sql.PreparedStatement;
import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class RoomDao {

    private final JdbcTemplate jdbcTemplate;

    public RoomDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public RoomEntity findById(final int roomId) {
        final String sql = "SELECT * FROM room WHERE room_id = ?";
        final RowMapper<RoomEntity> rowMapper = createRoomEntityRowMapper();
        return jdbcTemplate.queryForObject(sql, rowMapper, roomId);
    }

    public List<RoomEntity> findAllEntity() {
        final String sql = "SELECT * FROM room WHERE is_delete = ?";
        final RowMapper<RoomEntity> rowMapper = createRoomEntityRowMapper();
        return jdbcTemplate.query(sql, rowMapper, false);
    }

    private RowMapper<RoomEntity> createRoomEntityRowMapper() {
        return (resultSet, rowNum) -> new RoomEntity(
                Integer.parseInt(resultSet.getString("room_id")),
                resultSet.getString("name"),
                resultSet.getString("game_status"),
                resultSet.getString("current_turn"),
                resultSet.getString("password"),
                Boolean.parseBoolean(resultSet.getString("is_delete"))
        );
    }

    public int save(final RoomEntity roomEntity) {
        final String sql = "INSERT INTO room (name, game_status, current_turn, password) VALUES (?, ?, ?, ?)";
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        final PreparedStatementCreator statementCreator = con -> {
            final PreparedStatement ps = con.prepareStatement(sql, new String[]{"room_id"});
            ps.setString(1, roomEntity.getName());
            ps.setString(2, roomEntity.getGameStatus());
            ps.setString(3, roomEntity.getCurrentTurn());
            ps.setString(4, roomEntity.getPassword());
            return ps;
        };
        jdbcTemplate.update(statementCreator, keyHolder);
        return keyHolder.getKey().intValue();
    }

    public boolean isExistName(final String roomName) {
        final String sql = "SELECT EXISTS(SELECT name FROM (SELECT name FROM room WHERE is_delete = ?) AS r WHERE r.name = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, false, roomName);
    }

    public List<RoomResponseDto> findAll() {
        final String sql = "SELECT room_id, name, game_status FROM room WHERE is_delete = ?";
        final RowMapper<RoomResponseDto> rowMapper = (resultSet, rowNum) -> RoomResponseDto.of(
                Integer.parseInt(resultSet.getString("room_id")),
                resultSet.getString("name"),
                resultSet.getString("game_status"));
        return jdbcTemplate.query(sql, rowMapper, false);
    }

    public String findPasswordById(final int roomId) {
        try {
            final String sql = "SELECT password FROM room WHERE room_id = ? AND is_delete = ?";
            return jdbcTemplate.queryForObject(sql, String.class, roomId, false);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("방 아이디에 해당하는 비밀번호가 존재하지 않습니다.");
        }
    }

    public CurrentTurnDto findCurrentTurnById(final int roomId) {
        try {
            final String sql = "SELECT name, current_turn FROM room WHERE room_id = ? AND is_delete = ?";
            final RowMapper<CurrentTurnDto> rowMapper = (resultSet, rowNum) -> CurrentTurnDto.of(
                    resultSet.getString("name"),
                    Color.from(resultSet.getString("current_turn")));
            return jdbcTemplate.queryForObject(sql, rowMapper, roomId, false);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("방 아이디에 해당하는 턴 정보가 존재하지 않습니다.");
        }
    }

    public RoomStatusDto findStatusById(final int roomId) {
        try {
            final String sql = "SELECT name, game_status FROM room WHERE room_id = ? AND is_delete = ?";
            final RowMapper<RoomStatusDto> rowMapper = (resultSet, rowNum) -> RoomStatusDto.of(
                    resultSet.getString("name"),
                    resultSet.getString("game_status"));
            return jdbcTemplate.queryForObject(sql, rowMapper, roomId, false);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("방 아이디에 해당하는 게임 상태가 존재하지 않습니다.");
        }
    }

    public GameStatus findStatus(final int roomId) {
        final String sql = "SELECT game_status FROM room WHERE room_id = ?";
        final RowMapper<GameStatus> rowMapper = (resultSet, rowNum) ->
                GameStatus.from(resultSet.getString("game_status"));
        return jdbcTemplate.queryForObject(sql, rowMapper, roomId);
    }

    public int deleteById(final int roomId) {
        final String sql = "UPDATE room SET is_delete = ? WHERE room_id = ?";
        return jdbcTemplate.update(sql, true, roomId);
    }

    public int updateById(final int roomId, final GameStatus gameStatus, final Color currentTurn) {
        final String sql = "UPDATE room SET game_status = ?, current_turn = ? WHERE room_id = ?";
        return jdbcTemplate.update(sql, gameStatus.getValue(), currentTurn.getValue(), roomId);
    }

    public int updateStatusById(final int roomId, final GameStatus gameStatus) {
        final String sql = "UPDATE room SET game_status = ? WHERE room_id = ?";
        return jdbcTemplate.update(sql, gameStatus.getValue(), roomId);
    }
}
