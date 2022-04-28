package chess.domain.dao;

import chess.service.dto.GameDto;
import chess.domain.game.Status;
import chess.domain.game.board.ChessBoard;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class GameDao {

    private static final int EMPTY_RESULT = 0;

    private final JdbcTemplate jdbcTemplate;

    public GameDao(JdbcTemplate jdbcTemplate) {
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

    public int create(ChessBoard chessBoard, String title, int password) {
        final String sql = "insert into Game (status, turn, title, password) values( ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                statement.setBoolean(1, chessBoard.compareStatus(Status.PLAYING));
                statement.setString(2, String.valueOf(chessBoard.getCurrentTurn()));
                statement.setString(3, title);
                statement.setInt(4, password);
                return statement;
            }, keyHolder);
            return keyHolder.getKey().intValue();
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
                rs.getBoolean("status"),
                rs.getString("turn")
        );
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
