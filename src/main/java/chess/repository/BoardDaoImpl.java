package chess.repository;

import chess.domain.chessboard.ChessBoard;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BoardDaoImpl implements BoardDao {

    private JdbcTemplate jdbcTemplate;

    public BoardDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(ChessBoard chessBoard, int gameId) {
        String sql = "insert into board (position, symbol, color, game_id) values (?, ?, ?, ?)";

        List<Object[]> board = chessBoard.getPieces().entrySet().stream()
                .map(entry -> new Object[]{entry.getKey().toString(), entry.getValue().getSymbol().name(),
                        entry.getValue().getColor().name(), gameId})
                .collect(Collectors.toList());

        jdbcTemplate.batchUpdate(sql, board);
    }

    @Override
    public ChessBoard findById(int id) {
        String sql = "select position, symbol, color from board where game_id = ?";

        List<Map<String, Object>> squares = jdbcTemplate.queryForList(sql, id);
        Map<Position, Piece> board = new HashMap<>();
        for (Map<String, Object> square : squares) {
            board.put(
                    Position.of((String) square.get("position")),
                    Piece.of((String) square.get("color"), (String) square.get("symbol")));
        }
        return new ChessBoard(board);
    }

    @Override
    public int update(Position position, Piece piece, int gameId) {
        String sql = "update board set symbol = (?), color = (?) where game_id = (?) and position = (?)";
        return jdbcTemplate.update(sql, piece.getSymbol().name(), piece.getColor().name(), gameId, position.toString());
    }

    @Override
    public void delete(int gameId) {
        String sql = "delete from board where game_id = ?";
        jdbcTemplate.update(sql, gameId);
    }
}
