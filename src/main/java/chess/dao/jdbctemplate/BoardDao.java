package chess.dao.jdbctemplate;

import chess.domain.chessboard.ChessBoard;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BoardDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void save(ChessBoard chessBoard, Long gameId) {
        String sql = "insert into board (position, symbol, color, game_id) values (?, ?, ?, ?)";

        List<Object[]> board = chessBoard.getPieces().entrySet().stream()
                .map(entry -> new Object[]{entry.getKey().toString(), entry.getValue().getSymbol().name(),
                        entry.getValue().getColor().name(), gameId})
                .collect(Collectors.toList());

        jdbcTemplate.batchUpdate(sql, board);
    }

    public ChessBoard find() {
        String sql = "select position, symbol, color from board";

        List<Map<String, Object>> squares = jdbcTemplate.queryForList(sql);
        Map<Position, Piece> board = new HashMap<>();
        for (Map<String, Object> square : squares) {
            board.put(
                    Position.of((String) square.get("position")),
                    Piece.of((String) square.get("color"), (String) square.get("symbol")));
        }
        return new ChessBoard(board);
    }

    public int update(Position position, Piece piece, Long gameId) {
        String sql = "update board set symbol = (?), color = (?) where game_id = (?) and position = (?)";
        return jdbcTemplate.update(sql, piece.getSymbol().name(), piece.getColor().name(), gameId, position.toString());
    }
}
