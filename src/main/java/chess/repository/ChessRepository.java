package chess.repository;

import chess.domain.game.ChessGame;
import chess.domain.piece.Color;
import chess.utils.Serializer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ChessRepository {

    private final JdbcTemplate jdbcTemplate;

    public ChessRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<String> findGame(String title) {
        String findTitleQuery = "SELECT id FROM chess_game WHERE BINARY title = ?";
        return jdbcTemplate.queryForList(findTitleQuery, String.class, title)
                .stream()
                .findAny();
    }

    public Long addGame(ChessGame chessGame, String title) {
        String addingGameQuery = "INSERT INTO chess_game (turn, finished, board, title) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(
                addingGameQuery,
                chessGame.getTurn(),
                chessGame.isGameOver(),
                Serializer.serializeGame(chessGame),
                title);
        String findingGameQuery = "SELECT MAX(id) FROM chess_game";
        return jdbcTemplate.queryForObject(findingGameQuery, Long.class);
    }

    public ChessGame loadGame(Long id) {
        String loadingGameQuery = "SELECT * FROM chess_game WHERE id= ?";
        return jdbcTemplate.queryForObject(loadingGameQuery, chessGameMapper(), id);
    }

    public void saveGame(Long id, ChessGame chessGame) {
        String savingGameQuery = "UPDATE chess_game SET turn = ?, board = ? WHERE id = ?";
        jdbcTemplate.update(savingGameQuery, chessGame.getTurn(), Serializer.serializeGame(chessGame), id);
    }

    public void terminateGame(Long id) {
        String savingGameQuery = "UPDATE chess_game SET finished = ? WHERE id = ?";
        jdbcTemplate.update(savingGameQuery, true, id);
    }

    public void restart(Long id, ChessGame chessGame) {
        String restartQuery = "UPDATE chess_game SET turn = ?, finished = ?, board = ? WHERE id = ?";
        jdbcTemplate.update(restartQuery, chessGame.getTurn(), chessGame.isGameOver(), Serializer.serializeGame(chessGame),
                id);
    }

    public List<ChessGame> findAllGames() {
        String findAllQuery = "SELECT * FROM chess_game";
        return jdbcTemplate.query(findAllQuery, chessGameMapper());
    }

    private RowMapper<ChessGame> chessGameMapper() {
        return (rs, rowNum) -> new ChessGame(
                rs.getLong("id"),
                Color.of(rs.getString("turn")),
                rs.getBoolean("finished"),
                Serializer.deserializeGame(rs.getString("board")),
                rs.getString("title")
        );
    }
}
