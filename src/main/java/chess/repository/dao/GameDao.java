
package chess.repository.dao;

import chess.domain.ChessGameManager;
import chess.domain.piece.Color;
import chess.dto.GameEntryDto;
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

    public long save(ChessGameManager chessGameManager, String title) {
        Color currentTurnColor = chessGameManager.getCurrentTurnColor();

        String insertGameQuery = "INSERT INTO game(title, turn) VALUES(?, ?)";
        this.jdbcTemplate.update(insertGameQuery, title, currentTurnColor.name());

        String findGameIdQuery = "SELECT last_insert_id()";
        long gameId = this.jdbcTemplate.queryForObject(findGameIdQuery, Long.class);

        return gameId;
    }

    public Color findCurrentTurnByGameId(long gameId) {
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

    public void updateTurnByGameId(ChessGameManager chessGameManager, long gameId) {
        Color currentTurnColor = chessGameManager.getCurrentTurnColor();
        String query = "UPDATE game set turn=? WHERE game_id = ?";
        this.jdbcTemplate.update(query, currentTurnColor.name(), gameId);
    }

    public List<GameEntryDto> findAllGames() {
        String query = "SELECT game_id, title FROM game ";
        return this.jdbcTemplate.query(query, (resultSet, rowNum) -> {
            long gameId = resultSet.getLong("game_id");
            String gameTitle = resultSet.getString("title");
            return new GameEntryDto(gameId, gameTitle);
        });
    }

    @Override
    public void delete(long gameId) {
        String query = "DELETE from game WHERE game_id = ?";
        this.jdbcTemplate.update(query, gameId);
    }
}

