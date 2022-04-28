package chess.domain.dao;

import chess.domain.game.Status;
import chess.domain.game.board.ChessBoard;
import chess.service.dto.GameDto;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Repository
public class GameDao {

    private static final int EMPTY_RESULT = 0;

    private final JdbcTemplate jdbcTemplate;

    public GameDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int create(ChessBoard chessBoard, String title, String password) {
        final String sql = "insert into Game (status, turn, title, password) values( ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                statement.setBoolean(1, chessBoard.compareStatus(Status.PLAYING));
                statement.setString(2, String.valueOf(chessBoard.getCurrentTurn()));
                statement.setString(3, title);
                statement.setString(4, password);
                return statement;
            }, keyHolder);
            return keyHolder.getKey().intValue();
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new IllegalArgumentException("요청이 정상적으로 실행되지 않았습니다.");
        }
    }

    public List<GameDto> findAll() {
        final String sql = "select * from game";
        try {
            return jdbcTemplate.query(sql, (rs, rowNum) -> makeGameDto(rs));
        } catch (EmptyResultDataAccessException noResult) {
            return null;
        } catch (Exception e) {
            throw new IllegalArgumentException("요청이 정상적으로 실행되지 않았습니다.");
        }
    }

    public GameDto findById(int id) {
        final String sql = "select * from game where id = ?";
        try {
            GameDto result = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeGameDto(rs), id);
            return result;
        } catch (EmptyResultDataAccessException exception) {
            return null;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new IllegalArgumentException("요청이 정상적으로 실행되지 않았습니다.");
        }
    }

    private GameDto makeGameDto(ResultSet rs) throws SQLException {
        return new GameDto(
                rs.getInt("id"),
                rs.getString("title"),
                rs.getString("password"),
                rs.getBoolean("status"),
                rs.getString("turn")
        );
    }

    public void updateTurn(String turn, int gameId) {
        final String sql = "update Game set turn = ? where id = ?";
        try {
            jdbcTemplate.update(sql, turn, gameId);
        } catch (Exception e) {
            throw new IllegalArgumentException("요청이 정상적으로 실행되지 않았습니다.");
        }
    }

    public void updateStatus(int gameId) {
        final String sql = "update Game set status = ? where id = ?";
        try {
            jdbcTemplate.update(sql, false, gameId);
        } catch (Exception e) {
            throw new IllegalArgumentException("요청이 정상적으로 실행되지 않았습니다.");
        }
    }

    public void delete(int gameId) {
        final String sql = "delete from game where id = ?";
        try {
            jdbcTemplate.update(sql, gameId);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new IllegalArgumentException("요청이 정상적으로 실행되지 않았습니다.");
        }
    }

    public void deleteAll() {
        final String sql = "delete from game";
        try {
            jdbcTemplate.update(sql);
            jdbcTemplate.update("alter table Game alter column id restart with 1");
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new IllegalArgumentException("요청이 정상적으로 실행되지 않았습니다.");
        }
    }
}