package chess.dao;

import chess.domain.game.GameId;
import chess.domain.piece.PieceColor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class GameDaoImpl implements GameDao {
    private static final String TABLE_NAME = "game";
    private static final String WHITE_TURN = "WHITE";
    private static final String BLACK_TURN = "BLACK";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GameDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createGame(GameId gameId) {
        String query = String.format("INSERT INTO %s VALUES (?, 'WHITE')", TABLE_NAME);
        jdbcTemplate.update(query, gameId.getGameId());
    }

    @Override
    public void deleteGame(GameId gameId) {
        String query = String.format("DELETE FROM %s WHERE id = ?", TABLE_NAME);
        jdbcTemplate.update(query, gameId.getGameId());
    }

    @Override
    public void updateTurnToWhite(GameId gameId) {
        updateTurn(gameId, WHITE_TURN);
    }

    @Override
    public void updateTurnToBlack(GameId gameId) {
        updateTurn(gameId, BLACK_TURN);
    }

    private void updateTurn(GameId gameId, String turn) {
        String query = String.format("UPDATE %s SET turn = ? WHERE id = ?", TABLE_NAME);
        jdbcTemplate.update(query, turn, gameId.getGameId());
    }

    @Override
    public PieceColor getCurrentTurn(GameId gameId) {
        String query = String.format("SELECT turn FROM %s WHERE id = ?", TABLE_NAME);
        String turn = jdbcTemplate.queryForObject(query, (resultSet, rowNum) -> resultSet.getString("turn"),
                gameId.getGameId());
        return PieceColor.valueOf(turn);
    }
}
