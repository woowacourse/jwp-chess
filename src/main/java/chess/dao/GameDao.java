package chess.dao;

import chess.domain.game.Room;
import chess.domain.piece.Color;
import chess.domain.game.LogIn;
import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class GameDao {

    private static final String NO_GAME_ERROR_MESSAGE = "해당 제목의 방을 찾을 수 없습니다.";

    private final JdbcTemplate jdbcTemplate;

    public GameDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void create(LogIn logIn) {
        System.err.println("생성" + logIn);
        final String sql = "insert into game (id, password, turn) values (?, ?, ?)";
        jdbcTemplate.update(sql, logIn.getId(), logIn.getPassword(), Color.BLACK.getName());
    }

    public Room findNoPasswordRoom(String id) {
        final String sql = "select * from game where id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new Room(
                            rs.getString("id"),
                            Color.of(rs.getString("turn")),
                            rs.getBoolean("force_end_flag")),
                    id);
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException(NO_GAME_ERROR_MESSAGE);
        }
    }

    public Room findRoom(String id) {
        final String sql = "select * from game where id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new Room(
                            rs.getString("id"),
                            rs.getString("password"),
                            Color.of(rs.getString("turn")),
                            rs.getBoolean("force_end_flag"))
                    , id);
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException(NO_GAME_ERROR_MESSAGE);
        }
    }

    public List<Room> findAllRoom() {
        final String sql = "select * from game";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Room(
                rs.getString("id"),
                rs.getString("password"),
                Color.of(rs.getString("turn")),
                rs.getBoolean("force_end_flag")
        ));
    }

    public void updateTurn(Color nextTurn, String id) {
        final String sql = "update game set turn = ? where id = ?";
        jdbcTemplate.update(sql, nextTurn.getName(), id);
    }

    public void updateForceEndFlag(boolean forceEndFlag, String id) {
        final String sql = "update game set force_end_flag = ? where id = ?";
        jdbcTemplate.update(sql, forceEndFlag, id);
    }

    public void delete(String id) {
        final String sql = "delete from game where id = ?";
        jdbcTemplate.update(sql, id);
    }
}
