package chess.service.dao;

import chess.domain.ChessGame;
import chess.domain.board.Board;
import chess.domain.board.position.Horizontal;
import chess.domain.board.position.Position;
import chess.domain.board.position.Vertical;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceSymbolMapper;
import chess.domain.player.Turn;
import chess.util.JSonHandler;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class GameDao {
    private static final String COLUMN_LABEL_OF_TURN = "turn";
    private static final String COLUMN_LABEL_OF_BOARD = "board";

    private final JdbcTemplate jdbcTemplate;
    private final JSonHandler jSonHandler;

    public GameDao(final JdbcTemplate jdbcTemplate, final JSonHandler jSonHandler) {
        this.jdbcTemplate = jdbcTemplate;
        this.jSonHandler = jSonHandler;
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

    private String boardToData(final Board board) {
        return jSonHandler.mapToJsonData(board.parseUnicodeBoardAsMap());
    }

    private Board dataToBoard(final String dataLine) {
        final Map<String, String> unicodeMap = jSonHandler.jsonDataToStringMap(dataLine);
        final Map<Position, Piece> board = new HashMap<>();
        for (final Horizontal h : Horizontal.values()) {
            for (final Vertical v : Vertical.values()) {
                final Position position = new Position(v, h);
                final String unicode = unicodeMap.get(position.parseAsString());
                board.put(position, PieceSymbolMapper.parseToPiece(unicode));
            }
        }
        return new Board(board);
    }
}
