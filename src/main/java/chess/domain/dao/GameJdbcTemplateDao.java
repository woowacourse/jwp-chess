package chess.domain.dao;

import chess.domain.dto.GameDto;
import chess.domain.game.Status;
import chess.domain.game.board.ChessBoard;
import chess.domain.game.status.Playing;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

// GameJdbcTemplateDao -> GameDao
@Repository
public class GameJdbcTemplateDao {

    private static final int EMPTY_RESULT = 0;

    private final JdbcTemplate jdbcTemplate;

    public GameJdbcTemplateDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int save(ChessBoard chessBoard) {
        final String sql = "insert into Game (status, turn) values( ?, ?)";
        try {
            jdbcTemplate.update(sql, chessBoard.compareStatus(Status.PLAYING), chessBoard.getCurrentTurn().name());
            return findLastGameId();
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new IllegalArgumentException("요청이 정상적으로 실행되지 않았습니다.");
        }
    }

    public int findLastGameId() {
        final String sql = "SELECT id FROM Game ORDER BY id DESC LIMIT 1";
        try {
            Integer result = jdbcTemplate.queryForObject(sql, Integer.class);
            return result;
        } catch (EmptyResultDataAccessException exception) {
            return EMPTY_RESULT;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new IllegalArgumentException("요청이 정상적으로 실행되지 않았습니다.");
        }
    }

    public GameDto findById(int id) {
        final String sql = "select id, status, turn from game where id = ?";
        try {
            GameDto result = jdbcTemplate.queryForObject(sql, new RowMapper<GameDto>() {
                @Override
                public GameDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new GameDto(
                            rs.getInt("id"),
                            rs.getBoolean("status"),
                            rs.getString("turn")
                    );
                }
            }, id);
            return result;
        } catch (EmptyResultDataAccessException exception) {
            return null;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new IllegalArgumentException("요청이 정상적으로 실행되지 않았습니다.");
        }
    }

    public void delete() {
        final String sql = "delete from game where id = ?";
        try {
            int lastGameId = findLastGameId();
            jdbcTemplate.update(sql, lastGameId);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new IllegalArgumentException("요청이 정상적으로 실행되지 않았습니다.");
        }
    }

    public void deleteAll() {
        final String sql = "delete from game";
        try {
            jdbcTemplate.update(sql);
            jdbcTemplate.update("alter table game auto_increment = 1");
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new IllegalArgumentException("요청이 정상적으로 실행되지 않았습니다.");
        }
    }
}
