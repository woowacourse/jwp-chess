package chess.dao;

import chess.domain.player.Team;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ChessGameDao {

    private JdbcTemplate jdbcTemplate;

    public ChessGameDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int findChessGameIdByName(final String gameName) {
        final String sql = "select id from chess_game where name = (?)";
        return jdbcTemplate.queryForObject(sql, Integer.class, gameName);
    }

    public boolean isDuplicateGameName(final String gameName) {
        final String sql = "select id from chess_game where name = (?)";
        try {
            jdbcTemplate.queryForObject(sql, Integer.class, gameName);
            return true;
        } catch (DataAccessException e) {
            return false;
        }
    }

    public void saveChessGame(final String gameName, final Team turn) {
        final String sql = "insert into chess_game (name, turn) values (?, ?)";
        jdbcTemplate.update(sql, gameName, turn.getName());
    }

    public String findCurrentTurn(final int chessGameId) {
        final String sql = "select turn from chess_game where id = (?)";
        return jdbcTemplate.queryForObject(sql, String.class, chessGameId);
    }

    public void updateGameTurn(final int gameId, final Team nextTurn) {
        final String sql = "update chess_game set turn = (?) where id = (?)";
        jdbcTemplate.update(sql, nextTurn.getName(), gameId);
    }

    public void deleteChessGame(final int chessGameId) {
        final String sql = "delete from chess_game where id = (?)";
        jdbcTemplate.update(sql, chessGameId);
    }
}
