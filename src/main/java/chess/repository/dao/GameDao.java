package chess.repository.dao;

import chess.domain.ChessGameManager;
import chess.domain.piece.Color;
import chess.exception.NoSavedGameException;
import chess.repository.GameRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GameDao implements GameRepository {
    private final JdbcTemplate jdbcTemplate;

    public GameDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int saveNewGame(ChessGameManager chessGameManager) {
        Color currentTurnColor = chessGameManager.getCurrentTurnColor();

        String insertGameQuery = "insert into game(turn) values(?)";
        this.jdbcTemplate.update(insertGameQuery, currentTurnColor.name());

        String findGameIdQuery = "select last_insert_id()";
        int gameId = this.jdbcTemplate.queryForObject(findGameIdQuery, Integer.class);

        return gameId;
    }

    public Color loadCurrentTurnByGameId(int gameId) {
        String gameQuery = "SELECT turn FROM game WHERE game_id = ?";
        Color currentTurn = this.jdbcTemplate.queryForObject(gameQuery, colorRowMapper, gameId);

        if (currentTurn == null) {
            throw new NoSavedGameException("저장된 게임이 없습니다.");
        }
        return currentTurn;
    }

    private final RowMapper<Color> colorRowMapper = (resultSet, rowNum) -> {
        return Color.of(resultSet.getString("turn"));
    };

    public void updateTurnByGameId(ChessGameManager chessGameManager, int gameId) {
        Color currentTurnColor = chessGameManager.getCurrentTurnColor();
        String query = "UPDATE game set turn=? WHERE game_id = ?";
        this.jdbcTemplate.update(query, currentTurnColor.name(), gameId);
    }

    public List<Integer> loadGames() {
        String query = "SELECT game_id FROM game ";
        return this.jdbcTemplate.query(query, (resultSet, rowNum) -> resultSet.getInt("game_id"));
    }
}
