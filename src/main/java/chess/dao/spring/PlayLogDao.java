package chess.dao.spring;

import chess.domain.board.Board;
import chess.domain.chessgame.ChessGame;
import chess.dto.web.BoardDto;
import chess.dto.web.GameStatusDto;
import com.google.gson.Gson;
import java.util.Objects;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PlayLogDao {

    private static final Gson GSON = new Gson();

    private JdbcTemplate jdbcTemplate;

    public PlayLogDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insert(BoardDto boardDto, GameStatusDto gameStatusDto, String roomId) {
        String query = "INSERT INTO play_log (board, game_status, room_id) VALUES (?, ?, ?)";
        jdbcTemplate.update(query, GSON.toJson(boardDto), GSON.toJson(gameStatusDto), roomId);
    }

    public BoardDto latestBoard(String roomId) {
        String query = "SELECT board FROM play_log WHERE room_id = (?) ORDER BY last_played_time DESC, id DESC LIMIT 1";

        BoardDto result = jdbcTemplate.queryForObject(
            query,
            (resultSet, rowNum) -> {
                String boardJson = resultSet.getString(1);
                return GSON.fromJson(boardJson, BoardDto.class);
            },
            roomId);

        if (Objects.isNull(result)) {
            generateFirstRow(roomId);
            return latestBoard(roomId);
        }
        return result;
    }

    public GameStatusDto latestGameStatus(String roomId) {
        String query = "SELECT game_status FROM play_log WHERE room_id = (?) ORDER BY last_played_time DESC, id DESC LIMIT 1";

        GameStatusDto result = jdbcTemplate.queryForObject(
            query,
            (resultSet, rowNum) -> {
                String statusJson = resultSet.getString(1);
                return GSON.fromJson(statusJson, GameStatusDto.class);
            },
            roomId);

        if (Objects.isNull(result)) {
            generateFirstRow(roomId);
            return latestGameStatus(roomId);
        }
        return result;
    }

    private void generateFirstRow(String roomId) {
        Board board = new Board();
        BoardDto boardDto = new BoardDto(board);
        GameStatusDto gameStatusDto = new GameStatusDto(new ChessGame(board));
        insert(boardDto, gameStatusDto, roomId);
    }
}
