package chess.dao;

import chess.domain.piece.Color;
import chess.dto.SavedGameDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.Objects;

@Repository
public class GameDao {
    private final JdbcTemplate jdbcTemplate;

    public GameDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int saveGame(SavedGameDto savedGameDto) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String insertGameQuery = "INSERT INTO game(turn) VALUES(?)";
        this.jdbcTemplate.update(con -> {
            PreparedStatement pstmt = con.prepareStatement(
                    insertGameQuery,
                    new String[]{"game_id"});
            pstmt.setString(1, savedGameDto.getCurrentTurnColor());
            return pstmt;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    public String selectGameTurnByGameId(int gameId) {
        String gameQuery = "SELECT turn FROM game WHERE game_id = ?";
        return this.jdbcTemplate.queryForObject(gameQuery, String.class, gameId);
    }

    public void updateTurnByGameId(Color currentTurnColor, int gameId) {
        String query = "UPDATE game set turn=? WHERE game_id = ?";
        this.jdbcTemplate.update(query, currentTurnColor.name(), gameId);
    }

    public void deleteGameByGameId(int gameId) {
        this.jdbcTemplate.update("DELETE FROM game WHERE game_id = ?", gameId);
    }
}
