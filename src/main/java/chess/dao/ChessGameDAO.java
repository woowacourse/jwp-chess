package chess.dao;

import chess.domain.board.ChessBoard;
import chess.domain.board.ChessBoardGenerator;
import chess.domain.board.ChessGame;
import chess.dto.GameCreationDTO;
import java.sql.PreparedStatement;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class ChessGameDAO {

    private static final RowMapper<ChessGame> CHESS_GAME_ROW_MAPPER = (resultSet, rowNumber) ->
            new ChessGame(
                    resultSet.getString("id"),
                    resultSet.getString("name"),
                    resultSet.getString("password"),
                    resultSet.getBoolean("is_end"),
                    new ChessBoard(new ChessBoardGenerator())
            );

    private final JdbcTemplate jdbcTemplate;

    public ChessGameDAO(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public long addGame(final GameCreationDTO gameCreationDTO) {
        String sql = "INSERT INTO CHESS_GAME (name, password) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql, new String[]{"id"});
            statement.setString(1, gameCreationDTO.getName());
            statement.setString(2, gameCreationDTO.getPassword());
            return statement;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    public List<ChessGame> findAllGames() {
        String sql = "SELECT id, name, password, is_end FROM CHESS_GAME";
        return jdbcTemplate.query(sql, CHESS_GAME_ROW_MAPPER);
    }

    public ChessGame findGameById(final long gameId) {
        String sql = "SELECT id, name, password, is_end FROM CHESS_GAME WHERE ID = ?";
        return jdbcTemplate.queryForObject(sql, CHESS_GAME_ROW_MAPPER, gameId);
    }

    public long updateGameEnd(final long gameId) {
        String sql = "UPDATE chess_game SET is_end = true WHERE id = ?";

        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql, new String[]{"id"});
            statement.setLong(1, gameId);
            return statement;
        });
        return gameId;
    }

    public void deleteGame(final long gameId) {
        String sql = "DELETE FROM CHESS_GAME WHERE ID = ?";
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, gameId);
            return statement;
        });
    }
}
