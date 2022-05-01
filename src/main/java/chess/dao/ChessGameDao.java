package chess.dao;

import chess.domain.player.Team;
import chess.dto.response.ChessGameInfoDto;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ChessGameDao {

    private final JdbcTemplate jdbcTemplate;

    public ChessGameDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int saveChessGame(final String gameName, final String password, final String turn) {
        final String sql = "insert into chess_game (name, password, turn, running) values (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, gameName, password, turn, true);
    }

    public int findChessGameCountByName(final String gameName) {
        final String sql = "select count(*) from chess_game where name = (?)";
        return jdbcTemplate.queryForObject(sql, Integer.class, gameName);
    }

    public int findChessGameIdByName(final String gameName) {
        final String sql = "select id from chess_game where name = (?)";
        return jdbcTemplate.queryForObject(sql, Integer.class, gameName);
    }

    public String findCurrentTurn(final int chessGameId) {
        final String sql = "select turn from chess_game where id = (?)";
        return jdbcTemplate.queryForObject(sql, String.class, chessGameId);
    }

    public void deleteChessGame(final int chessGameId) {
        final String sql = "delete from chess_game where id = (?)";
        jdbcTemplate.update(sql, chessGameId);
    }

    public void updateGameTurn(final int gameId, final Team nextTurn) {
        final String sql = "update chess_game set turn = (?) where id = (?)";
        jdbcTemplate.update(sql, nextTurn.getName(), gameId);
    }

    public List<ChessGameInfoDto> findAllChessGame() {
        final String sql = "select id, name, turn, running from chess_game";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new ChessGameInfoDto(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("turn"),
                rs.getBoolean("running")
        ));
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
