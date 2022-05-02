package chess.db;

import chess.domain.piece.Color;
import chess.domain.piece.InitialPositionPieceGenerator;
import chess.domain.piece.Piece;
import chess.domain.position.Column;
import chess.domain.position.Row;
import chess.domain.position.Square;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PieceDao {
    private static final String ERROR_MESSAGE_NO_SAVED_GAME = "헉.. 저장 안한거 아냐? 그런 게임은 없어!";

    private final JdbcTemplate jdbcTemplate;

    public PieceDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void initPieces(String gameId) {
        String sql = "insert into piece (position, type, color, gameId) values (?, ?, ?, ?)";

        List<Object[]> data = new ArrayList<>();
        insertPieces(gameId, data);
        jdbcTemplate.batchUpdate(sql, data);
    }

    private void insertPieces(String gameId, List<Object[]> data) {
        for (Column column : Column.values()) {
            insertPiecesInRow(gameId, data, column);
        }
    }

    private void insertPiecesInRow(String gameId, List<Object[]> data, Column column) {
        for (Row row : Row.values()) {
            data.add(new Object[]{new Square(column, row).getName(),
                    InitialPositionPieceGenerator.getType(column, row).name(),
                    InitialPositionPieceGenerator.getColor(row).name(),
                    gameId});
        }
    }

    public void deleteByPosition(Square target, String gameId) {
        String sql = "delete from piece where position = ? and gameId = ?";
        jdbcTemplate.update(sql, target.getName(), gameId);
    }

    public void updatePosition(Square source, Square target, String gameId) {
        String sql = "update piece set position = ? where position = ? and gameId = ?";
        jdbcTemplate.update(sql, target.getName(), source.getName(), gameId);
    }

    public void insertNone(String gameId, Square source) {
        String sql = "insert into piece (position, type, color, gameId) values (?, ?, ?, ?)";
        jdbcTemplate.update(sql, source.getName(), InitialPositionPieceGenerator.NONE.name(), Color.NONE.name(), gameId);
    }

    public void deleteByGameId(String gameId) {
        String sql = "delete from piece where gameId = ?";
        jdbcTemplate.update(sql, gameId);
    }

    public Map<Square, Piece> findByGameId(String gameId) {
        String sql = "select position, type, color from piece where gameId = ?";
        Map<Square, Piece> board = new HashMap<>();
        List<Map<String, Object>> data = jdbcTemplate.queryForList(sql, gameId);
        loadBoard(board, data);
        checkGameExist(board);
        return board;
    }

    private void loadBoard(Map<Square, Piece> board, List<Map<String, Object>> pieces) {
        for (Map<String, Object> piece : pieces) {
            String position = (String) piece.get("position");
            String type = (String) piece.get("type");
            String color = (String) piece.get("color");
            board.put(new Square(position), Piece.createByTypeAndColor(type, color));
        }
    }

    private void checkGameExist(Map<Square, Piece> board) {
        if (board.isEmpty()) {
            throw new IllegalArgumentException(ERROR_MESSAGE_NO_SAVED_GAME);
        }
    }
}
