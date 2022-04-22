package chess.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import chess.dto.response.ChessGameDto;

@Repository
public class SpringGameDaoImpl implements GameDao {
    private static final String TABLE_NAME = "game";
    private static final String WHITE_TURN = "WHITE";
    private static final String BLACK_TURN = "BLACK";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public SpringGameDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ChessGameDto getGame(String gameId) {
        String query = String.format("SELECT turn FROM %s WHERE id = ?", TABLE_NAME);
        String turn = jdbcTemplate.queryForObject(query, (resultSet, rowNum) -> resultSet.getString("turn"), gameId);
        return ChessGameDto.of(gameId, turn);
    }

    @Override
    public void createGame(String gameId) {
        String query = String.format("INSERT INTO %s VALUES (?, 'WHITE')", TABLE_NAME);
        jdbcTemplate.update(query, gameId);
    }

    @Override
    public void deleteGame(String gameId) {
        String query = String.format("DELETE FROM %s WHERE id = ?", TABLE_NAME);
        jdbcTemplate.update(query, gameId);
    }

    @Override
    public void updateTurnToWhite(String gameId) {
        updateTurn(gameId, WHITE_TURN);
    }

    @Override
    public void updateTurnToBlack(String gameId) {
        updateTurn(gameId, BLACK_TURN);
    }

    private void updateTurn(String gameId, String turn) {
        String query = String.format("UPDATE %s SET turn = ? WHERE id = ?", TABLE_NAME);
        jdbcTemplate.update(query, turn, gameId);
    }

}
