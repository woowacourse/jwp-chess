package chess.dao;

import chess.domain.GameStatus;
import chess.domain.chesspiece.Color;
import chess.dto.response.CurrentTurnDto;
import chess.dto.response.RoomResponseDto;
import chess.dto.response.RoomStatusDto;
import chess.exception.NotFoundException;
import java.sql.PreparedStatement;
import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class RoomDao {

    private final JdbcTemplate jdbcTemplate;

    public RoomDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int save(final String roomName, final GameStatus gameStatus, final Color currentTurn,
                    final String password) {
        final String sql = "INSERT INTO room (name, game_status, current_turn, password) VALUES (?, ?, ?, ?)";
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            final PreparedStatement ps = con.prepareStatement(sql, new String[]{"room_id"});
            ps.setString(1, roomName);
            ps.setString(2, gameStatus.getValue());
            ps.setString(3, currentTurn.getValue());
            ps.setString(4, password);
            return ps;
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }

    public boolean isExistName(final String roomName) {
        final String sql = "SELECT EXISTS(SELECT name FROM room WHERE name = ?)";
        final String name = jdbcTemplate.queryForObject(sql, String.class, roomName);
        return name != null;
    }

    public boolean isExistId(final int roomId) {
        final String sql = "SELECT EXISTS(SELECT room_id FROM room WHERE room_id = ?)";
        final Integer result = jdbcTemplate.queryForObject(sql, Integer.class, roomId);
        return result != 0;
    }

    public List<RoomResponseDto> findAll() {
        final String sql = "SELECT room_id, name, game_status FROM room";
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> RoomResponseDto.from(resultSet));
    }

    public String findPasswordById(final int roomId) {
        try {
            final String sql = "SELECT password FROM room WHERE room_id = ?";
            return jdbcTemplate.queryForObject(sql, String.class, roomId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("방 아이디에 해당하는 비밀번호가 존재하지 않습니다.");
        }
    }

    public CurrentTurnDto findCurrentTurnById(final int roomId) {
        try {
            final String sql = "SELECT name, current_turn FROM room WHERE room_id = ?";
            return jdbcTemplate.queryForObject(sql, (resultSet, rowNum) -> CurrentTurnDto.from(resultSet), roomId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("방 아이디에 해당하는 턴 정보가 존재하지 않습니다.");
        }
    }

    public RoomStatusDto findStatusById(final int roomId) {
        try {
            final String sql = "SELECT name, game_status FROM room WHERE room_id = ?";
            return jdbcTemplate.queryForObject(sql, (resultSet, rowNum) -> RoomStatusDto.from(resultSet), roomId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("방 아이디에 해당하는 게임 상태가 존재하지 않습니다.");
        }
    }

    public int deleteById(final int roomId) {
        final String sql = "DELETE FROM room WHERE room_id = ?";
        return jdbcTemplate.update(sql, roomId);
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
