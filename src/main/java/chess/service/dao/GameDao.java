package chess.service.dao;

import chess.domain.ChessGame;
import chess.domain.board.Board;
import chess.domain.board.position.Horizontal;
import chess.domain.board.position.Position;
import chess.domain.board.position.Vertical;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceSymbolMapper;
import chess.domain.player.Turn;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

import static org.apache.logging.log4j.message.MapMessage.MapFormat.JSON;

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
        final String query = "INSERT INTO game (room_id, turn, board) VALUES (?, ?, ?)";
        jdbcTemplate.update(query, roomId, turn.name(), boardToData(board));
    }

    public ChessGame load(final long roomId) {
        final String query = "SELECT * FROM game WHERE room_id = (?) ORDER BY id DESC limit 1";
        return jdbcTemplate.queryForObject(query, (rs, rowNum) -> {
            final String board = rs.getString(COLUMN_LABEL_OF_BOARD);
            final String turn = rs.getString(COLUMN_LABEL_OF_TURN);
            return ChessGame.load(dataToBoard(board), Turn.of(turn));
        }, roomId);
    }

    public void delete(final long roomId) {
        final String query = "DELETE FROM game WHERE room_id = ?";
        jdbcTemplate.update(query, roomId);
    }

    public void update(final long roomId, final Turn turn, final Board board) {
        final String query = "UPDATE game SET turn = ?,  board= ?  WHERE room_id = ?";
        jdbcTemplate.update(query, turn.name(), boardToData(board), roomId);
    }

    public String boardToData(final Board board) {
        final Map<String, String> uniCodeBoard = board.parseUnicodeBoardAsMap();
        final List<String> symbols = new LinkedList<>();
        for (final Horizontal h : Horizontal.values()) {
            for (final Vertical v : Vertical.values()) {
                symbols.add(uniCodeBoard.get(new Position(v, h).parseAsString()));
            }
        }
        return symbols.stream().collect(Collectors.joining(SEPARATOR_OF_PIECE));
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
