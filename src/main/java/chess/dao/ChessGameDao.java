package chess.dao;

import chess.domain.player.Team;
import chess.exception.ExecuteQueryException;
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
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, gameName);
        } catch (DataAccessException e) {
            throw new ExecuteQueryException("체스 게임 정보 불러오는데 실패했습니다.");
        }
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
        try {
            jdbcTemplate.update(sql, gameName, turn.getName());
        } catch (DataAccessException e) {
            throw new ExecuteQueryException("게임을 저장할 수 없습니다.");
        }
    }

    public String findCurrentTurn(final int chessGameId) {
        final String sql = "select turn from chess_game where id = (?)";
        try {
            return jdbcTemplate.queryForObject(sql, String.class, chessGameId);
        } catch (DataAccessException e) {
            throw new ExecuteQueryException("현재 턴 정보 불러오기가 실패했습니다.");
        }
    }

    public void updateGameTurn(final int gameId, final Team nextTurn) {
        final String sql = "update chess_game set turn = (?) where id = (?)";
        try {
            jdbcTemplate.update(sql, nextTurn.getName(), gameId);
        } catch (DataAccessException e) {
            throw new ExecuteQueryException("턴 정보 업데이트에 실패했습니다.");
        }
    }

    public void deleteChessGame(final int chessGameId) {
        final String sql = "delete from chess_game where id = (?)";
        try {
            jdbcTemplate.update(sql, chessGameId);
        } catch (DataAccessException e) {
            throw new ExecuteQueryException("게임을 삭제할 수 없습니다.");
        }
    }
}
