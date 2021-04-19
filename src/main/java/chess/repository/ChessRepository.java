package chess.repository;

import chess.domain.game.ChessGame;
import chess.domain.piece.Color;
import chess.utils.Serializer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ChessRepository {
    private JdbcTemplate jdbcTemplate;

    public ChessRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<String> findRoomId(String title) {
        String findTitleQuery = "SELECT id FROM chess_game WHERE BINARY title = ?";
        return jdbcTemplate.queryForList(findTitleQuery, String.class, title)
                .stream()
                .findAny();
    }

    public String addGame(ChessGame chessGame, String title) {
        String addingGameQuery = "INSERT INTO chess_game (turn, finished, board, title) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(addingGameQuery, chessGame.getTurn(), chessGame.isOver(), Serializer.serializeGame(chessGame)
                , title);
        String findingGameQuery = "SELECT MAX(id) FROM chess_game";
        return jdbcTemplate.queryForObject(findingGameQuery, String.class);
    }

    public String loadGame(String gameId) {
        String loadingGameQuery = "SELECT board FROM chess_game WHERE id= ?";
        return jdbcTemplate.queryForObject(loadingGameQuery, String.class, gameId);
    }

    public ChessGame loadGameById(String gameId) {
        String findingGameQuery = "SELECT board, turn FROM chess_game WHERE id= ?";
        return jdbcTemplate.queryForObject(findingGameQuery, (resultSet, rowNum) -> {
            return new ChessGame(
                    Serializer.deserializeGame(resultSet.getString("board")),
                    Color.of(resultSet.getString("turn")));
        }, gameId);
    }

    public ChessGame loadGameByName(String roomName) {
        String findingGameQuery = "SELECT board, turn FROM chess_game WHERE BINARY title = ?";
        return jdbcTemplate.queryForObject(findingGameQuery, (resultSet, rowNum) -> {
            return new ChessGame(
                    Serializer.deserializeGame(resultSet.getString("board")),
                    Color.of(resultSet.getString("turn")));
        }, roomName);
    }

    public String turn(String gameId) {
        String findingTurnQuery = "SELECT turn FROM chess_game WHERE id = ?";
        return jdbcTemplate.queryForObject(findingTurnQuery, String.class, gameId);
    }

    public void saveGame(String gameId, ChessGame chessGame) {
        String savingGameQuery = "UPDATE chess_game SET turn = ?, board = ? WHERE id = ?";
        jdbcTemplate.update(savingGameQuery, chessGame.getTurn(), Serializer.serializeGame(chessGame), gameId);
    }

    public boolean isFinishedById(String gameId) {
        String finishedQuery = "SELECT finished FROM chess_game WHERE id = ?";
        return jdbcTemplate.queryForObject(finishedQuery, Boolean.class, gameId);
    }

    public boolean isFinishedByName(String roomName) {
        String finishedQuery = "SELECT finished FROM chess_game WHERE BINARY title = ?";
        return jdbcTemplate.queryForObject(finishedQuery, Boolean.class, roomName);
    }

    public void finish(String gameId) {
        String savingGameQuery = "UPDATE chess_game SET finished = ? WHERE id = ?";
        jdbcTemplate.update(savingGameQuery, true, gameId);
    }

    public void restart(String gameId, ChessGame chessGame) {
        String restartQuery = "UPDATE chess_game SET turn = ?, finished = ?, board = ? WHERE id = ?";
        jdbcTemplate.update(restartQuery, chessGame.getTurn(), chessGame.isOver(), Serializer.serializeGame(chessGame),
                gameId);
    }

    public Map<String, String> findAllRooms() {
        String findAllQuery = "SELECT id, title FROM chess_game";
        return jdbcTemplate
                .queryForList(findAllQuery)
                .stream()
                .collect(Collectors.toMap(room -> room.get("id").toString(), room -> room.get("title").toString()));
    }
}
