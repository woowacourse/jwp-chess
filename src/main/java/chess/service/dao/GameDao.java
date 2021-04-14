package chess.service.dao;

import chess.controller.dto.GameDto;
import chess.domain.board.Board;
import chess.domain.board.position.Horizontal;
import chess.domain.board.position.Position;
import chess.domain.board.position.Vertical;
import chess.domain.piece.Piece;
import chess.domain.player.Turn;
import chess.view.PieceSymbolMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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
        final String query = "SELECT * FROM game_status WHERE room_id = (?) ORDER BY id DESC limit 1";
        return jdbcTemplate.queryForObject(query, (rs, rowNum)
                -> makeGameDto(rs.getString(COLUMN_LABEL_OF_TURN), rs.getString(COLUMN_LABEL_OF_BOARD)), roomId);
    }

    private GameDto makeGameDto(final String turn, final String board) {
        return new GameDto(Turn.of(turn), dataToBoard(board));
    }

    public void delete(final Long roomId) {
        final String query = "DELETE FROM game_status WHERE room_id = ?";
        jdbcTemplate.update(query, roomId);
    }

    public void update(final Long roomId, final Turn turn, final Board board) {
        final String query = "UPDATE game_status SET turn = ?,  board= ?  WHERE room_id = ?";
        jdbcTemplate.update(query, turn.name(), boardToData(board), roomId);
    }

    public String boardToData(final Board board) {
        return Arrays.stream(board.parseUnicodeBoard())
                .flatMap(strings -> Arrays.stream(strings))
                .collect(Collectors.joining(SEPARATOR_OF_PIECE));
    }

    public static Board dataToBoard(final String dataLine) {
        final Map<Position, Piece> board = new HashMap<>();
        final String[] pieces = dataLine.split(SEPARATOR_OF_PIECE);
        int index = 0;
        for (final Horizontal h : Horizontal.values()) {
            for (final Vertical v : Vertical.values()) {
                board.put(new Position(v, h), PieceSymbolMapper.parseToPiece(pieces[index++]));
            }
        }
        return new Board(board);
    }
}
