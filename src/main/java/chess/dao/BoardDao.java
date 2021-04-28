package chess.dao;

import chess.domain.board.Board;
import chess.domain.board.BoardFactory;
import chess.domain.board.Position;
import chess.domain.piece.Piece;
import chess.dto.BoardDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BoardDao {
    private static final String UPDATE_BOARD_QUERY = "update board set piece = ? where position = ? and room_id = ?";

    private final JdbcTemplate jdbcTemplate;

    public BoardDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public BoardDto createBoard(int roomId) {
        Board newBoard = BoardFactory.create();
        newBoard.getBoard().forEach((key, value) -> {
            String unicode = value != null ? value.getUnicode() : "";
            executeBoardInsertQuery(key.convertToString(), unicode, roomId);
        });
        return BoardDto.of(newBoard);
    }

    public BoardDto findBoardByRoomId(int roomId) {
        String sql = "select position, piece from board where room_id = ?;";
        List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql, roomId);
        Map<String, String> boardInfo = new HashMap<>();
        for (Map<String, Object> result : resultList) {
            String position = (String) result.get("position");
            String piece = (String) result.get("piece");
            boardInfo.put(position, piece);
        }
        Board board = Board.from(boardInfo);
        return BoardDto.of(board);
    }

    public void resetBoard(Board board, int roomId) {
        for (Map.Entry<Position, Piece> entry : board.getBoard().entrySet()) {
            Position position = entry.getKey();
            Piece piece = entry.getValue();
            String unicode = piece != null ? piece.getUnicode() : "";
            executeBoardUpdateQuery(unicode, position.convertToString(), roomId);
        }
    }

    private void executeBoardInsertQuery(String position, String unicode, int roomId) {
        String sql = "insert into board (position, piece, room_id) values (?, ?, ?)";
        jdbcTemplate.update(sql, position, unicode, roomId);
    }

    private void executeBoardUpdateQuery(String unicode, String position, int roomId) {
        jdbcTemplate.update(UPDATE_BOARD_QUERY, unicode, position, roomId);
    }

    public void updateBoardAfterMove(String targetPosition, String destinationPosition, Piece targetPiece, int roomId) {
        jdbcTemplate.update(UPDATE_BOARD_QUERY, targetPiece.getUnicode(), destinationPosition, roomId);
        jdbcTemplate.update(UPDATE_BOARD_QUERY, "", targetPosition, roomId);
    }
}
