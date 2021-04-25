package chess.repository;

import chess.domain.board.Board;
import chess.domain.chessgame.ChessGame;
import chess.dto.web.BoardDto;
import chess.dto.web.GameStatusDto;
import com.google.gson.Gson;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PlayLogRepository {

    private final JdbcTemplate jdbcTemplate;
    private final Gson gson;

    public PlayLogRepository(JdbcTemplate jdbcTemplate, Gson gson) {
        this.jdbcTemplate = jdbcTemplate;
        this.gson = gson;
    }

    public void insert(BoardDto boardDto, GameStatusDto gameStatusDto, String roomId) {
        String query = "INSERT INTO play_log (board, game_status, room_id) VALUES (?, ?, ?)";
        jdbcTemplate.update(query, gson.toJson(boardDto), gson.toJson(gameStatusDto), roomId);
    }

    public BoardDto latestBoard(String roomId) {
        String query = "SELECT board FROM play_log WHERE room_id = (?) ORDER BY last_played_time DESC, id DESC LIMIT 1";

        try {
            BoardDto result = jdbcTemplate.queryForObject(
                    query,
                    (resultSet, rowNum) -> {
                        String boardJson = resultSet.getString(1);
                        return gson.fromJson(boardJson, BoardDto.class);
                    },
                    roomId);
            return result;
        } catch (Exception e) {
            generateFirstRow(roomId);
            return latestBoard(roomId);
        }
    }

    public GameStatusDto latestGameStatus(String roomId) {
        String query = "SELECT game_status FROM play_log WHERE room_id = (?) ORDER BY last_played_time DESC, id DESC LIMIT 1";

        try {
            return jdbcTemplate.queryForObject(
                    query,
                    (resultSet, rowNum) -> {
                        String statusJson = resultSet.getString(1);
                        return gson.fromJson(statusJson, GameStatusDto.class);
                    },
                    roomId);
        } catch (Exception e) {
            generateFirstRow(roomId);
            return latestGameStatus(roomId);
        }
    }

    private void generateFirstRow(String roomId) {
        Board board = new Board();
        BoardDto boardDto = new BoardDto(board);
        GameStatusDto gameStatusDto = new GameStatusDto(new ChessGame(board));
        insert(boardDto, gameStatusDto, roomId);
    }
}
