package chess.dao;

import chess.domain.ChessGame;
import chess.domain.player.Team;
import chess.dto.response.ChessGameInfoDto;
import chess.exception.ExecuteQueryException;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ChessGameDao {

    private final JdbcTemplate jdbcTemplate;

    public ChessGameDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createNewChessGame(final ChessGame chessGame, final String gameName, final String password) {
        validateDuplicate(gameName);
        saveChessGame(gameName, password, chessGame.getTurn());
    }

    private void validateDuplicate(final String gameName) {
        final String sql = "select count(*) from chess_game where name = (?)";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, gameName);

        if (count > 0) {
            throw new IllegalArgumentException("중복된 게임 이름입니다.");
        }
    }

    private void saveChessGame(final String gameName, final String password, final Team turn) {
        final String sql = "insert into chess_game (name, password, turn, running) values (?, ?, ?, ?)";
        try {
            jdbcTemplate.update(sql, gameName, password, turn.getName(), true);
        } catch (DataAccessException e) {
            throw new ExecuteQueryException("게임을 저장할 수 없습니다.");
        }
    }

    public int findChessGameIdByName(final String gameName) {
        final String sql = "select id from chess_game where name = (?)";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, gameName);
        } catch (DataAccessException e) {
            throw new ExecuteQueryException("체스 게임 정보 불러오는데 실패했습니다.");
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

    public void deleteChessGame(final int chessGameId) {
        final String sql = "delete from chess_game where id = (?)";
        try {
            jdbcTemplate.update(sql, chessGameId);
        } catch (DataAccessException e) {
            throw new ExecuteQueryException("게임을 삭제할 수 없습니다.");
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

    public List<ChessGameInfoDto> findAllChessGame() {
        final String sql = "select id, name, turn, running from chess_game";
        try {
            return jdbcTemplate.query(sql, (rs, rowNum) -> new ChessGameInfoDto(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("turn"),
                    rs.getBoolean("running")
            ));
        } catch (DataAccessException e) {
            throw new ExecuteQueryException("체스 게임 정보 불러오기를 실패했습니다.");
        }
    }

    public int findChessGameByIdAndPassword(final int gameId, final String password) {
        final String sql = "select count(*) from chess_game where id = (?) and password = (?)";
        return jdbcTemplate.queryForObject(sql, Integer.class, gameId, password);
    }

    public ChessGameInfoDto findChessGame(final int gameId) {
        final String sql = "select id, name, turn, running from chess_game where id = (?)";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new ChessGameInfoDto(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("turn"),
                rs.getBoolean("running")
        ), gameId);
    }
}
