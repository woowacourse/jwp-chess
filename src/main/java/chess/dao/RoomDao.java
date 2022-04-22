package chess.dao;

import chess.domain.GameStatus;
import chess.domain.chesspiece.Color;
import chess.dto.CurrentTurnDto;
import chess.dto.RoomStatusDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RoomDao {

    private final JdbcTemplate jdbcTemplate;

    public RoomDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int save(final String roomName, final GameStatus gameStatus, final Color currentTurn) {
        final String sql = "INSERT INTO room (name, game_status, current_turn) VALUE (?, ?, ?)";
        return jdbcTemplate.update(sql, roomName, gameStatus.getValue(), currentTurn.getValue());
    }

    public boolean isExistName(final String roomName) {
        final String sql = "SELECT name FROM room WHERE name = ?";
        final String name = jdbcTemplate.queryForObject(sql, String.class, roomName);
        return name != null;
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

    public int updateStatusTo(final String roomName, final GameStatus gameStatus) {
        final String sql = "UPDATE room SET game_status = ? WHERE name = ?";
        return jdbcTemplate.update(sql, gameStatus.getValue(), roomName);
    }
}
