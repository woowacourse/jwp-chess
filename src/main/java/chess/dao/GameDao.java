package chess.dao;

import chess.domain.piece.Color;
import chess.dto.LogInDto;
import chess.entity.RoomEntity;
import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class GameDao {

    private static final String NO_GAME_ERROR_MESSAGE = "해당 제목의 방을 찾을 수 없습니다.";
    private static final String ALL_NO_ROOM_ERROR_MESSAGE = "현재 방이 없습니다.";

    private final JdbcTemplate jdbcTemplate;

    public GameDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void create(LogInDto logInDto) {
        final String sql = "insert into game (id, password, turn) values (?, ?, ?)";

        jdbcTemplate.update(sql, logInDto.getGameId(), logInDto.getGamePassword(), Color.BLACK.getName());
    }

    public RoomEntity findRoom(LogInDto logInDto) {
        final String sql = "select * from game where id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new RoomEntity(
                    rs.getString("id"),
                    rs.getString("password"),
                    Color.of(rs.getString("turn")),
                    rs.getBoolean("force_end_flag")
            ), logInDto.getGameId());
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException(NO_GAME_ERROR_MESSAGE);
        }
    }

    public RoomEntity findRoom(String gameId) {
        final String sql = "select * from game where id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new RoomEntity(
                    rs.getString("id"),
                    rs.getString("password"),
                    Color.of(rs.getString("turn")),
                    rs.getBoolean("force_end_flag")
            ), gameId);
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException(NO_GAME_ERROR_MESSAGE);
        }
    }

    public boolean findForceEndFlag(String gameId) {
        final String sql = "select force_end_flag from game where id = ?";
        try {
            return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, gameId));
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException(NO_GAME_ERROR_MESSAGE);
        }
    }

    public Color findTurn(String gameId) {
        final String sql = "select turn from game where id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (resultSet, rowNum) ->
                    Color.of(resultSet.getString("turn")), gameId);
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException(NO_GAME_ERROR_MESSAGE);
        }
    }

    public List<RoomEntity> findAllGame() {
        final String sql = "select * from game";
        try {
            return jdbcTemplate.query(sql, (rs, rowNum) -> new RoomEntity(
                    rs.getString("id"),
                    rs.getString("password"),
                    Color.of(rs.getString("turn")),
                    rs.getBoolean("force_end_flag")
            ));
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException(ALL_NO_ROOM_ERROR_MESSAGE);
        }
    }

    public void updateTurn(Color nextTurn, String gameId) {
        final String sql = "update game set turn = ? where id = ?";
        jdbcTemplate.update(sql, nextTurn.getName(), gameId);
    }

    public void updateForceEndFlag(boolean forceEndFlag, String gameId) {
        final String sql = "update game set force_end_flag = ? where id = ?";
        jdbcTemplate.update(sql, forceEndFlag, gameId);
    }

    public void delete(String gameId) {
        final String sql = "delete from game where id = ?";
        jdbcTemplate.update(sql, gameId);
    }

    public boolean isValidPassword(LogInDto logInDto) {
        final String sql = "select count(*) from game where id = ? and password = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, logInDto.getGameId(), logInDto.getGamePassword()) > 0;
    }

    public boolean isInId(String gameId) {
        final String sql = "select count(*) from game where id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, gameId) > 0;
    }
}
