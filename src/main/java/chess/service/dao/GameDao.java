package chess.service.dao;

import chess.controller.dto.GameDto;
import chess.domain.board.Board;
import chess.domain.board.position.Horizontal;
import chess.domain.board.position.Vertical;
import chess.domain.player.Turn;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class GameDao {
    private static final String COLUMN_LABEL_OF_TURN = "turn";
    private static final String COLUMN_LABEL_OF_BOARD = "board";
    private static final String SEPARATOR_OF_PIECE = ",";

    private final JdbcTemplate jdbcTemplate;

    public GameDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(final long roomId, final Turn turn, final Board board) {
        final String query = "INSERT INTO game_status (room_id, turn, board) VALUES (?, ?, ?)";
        jdbcTemplate.update(query, roomId, turn.name(), boardToData(board));
    }

    public GameDto load(final Long roomId) {
        final String query = "SELECT * FROM game_status WHERE room_id = ?";
        List<GameDto> results = jdbcTemplate.query(query, makeGameDto(), roomId);
        return results.stream()
                .findAny()
                .orElseThrow(() -> new IllegalStateException("게임 정보를 찾을 수 없습니다."));
    }

    public void delete(final Long roomId) {
        final String query = "DELETE FROM game_status WHERE room_id = ?";
        jdbcTemplate.update(query, roomId);
    }

    public void update(final Long roomId, final Turn turn, final Board board) {
        final String query = "UPDATE game_status SET turn = ?,  board= ?  WHERE room_id = ?";
        jdbcTemplate.update(query, turn.name(), boardToData(board), roomId);
    }

    private RowMapper<GameDto> makeGameDto() {
        return (rs, rowNum) -> {
            String turn = rs.getString(COLUMN_LABEL_OF_TURN);
            String board = rs.getString(COLUMN_LABEL_OF_BOARD);
            return new GameDto(turn, board);
        };
    }

    public String boardToData(final Board board) {
        List<String> boardData = new ArrayList<>();
        convertBoardToData(board, boardData);
        return String.join(SEPARATOR_OF_PIECE, boardData);
    }

    private void convertBoardToData(Board board, List<String> boardData) {
        for (Vertical v : Vertical.values()) {
            convertToData(board, boardData, v);
        }
    }

    private void convertToData(Board board, List<String> boardData, Vertical v) {
        for (Horizontal h : Horizontal.values()) {
            String position = v.getVertical() + h.getHorizontal();
            String pieceCode = board.toBoardDto().get(position);
            boardData.add(pieceCode);
        }
    }
}
