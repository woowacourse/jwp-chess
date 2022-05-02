package chess.dao;

import chess.domain.Color;
import chess.domain.board.Position;
import chess.domain.piece.Piece;
import chess.domain.piece.Type;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PieceJdbcTemplateDao implements PieceDao {
    private final JdbcTemplate jdbcTemplate;

    public PieceJdbcTemplateDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Map<Position, Piece> board, int boardId) {
        for (Entry<Position, Piece> positionPieceEntry : board.entrySet()) {
            executeInsertPiece(positionPieceEntry, boardId);
        }
    }

    private void executeInsertPiece(Entry<Position, Piece> positionPieceEntry, int boardId) {
        final String sql = "insert into piece (board_id, position, type, color) values (?, ?, ?, ?)";
        final Position position = positionPieceEntry.getKey();
        final Piece piece = positionPieceEntry.getValue();
        jdbcTemplate.update(sql, boardId, position.stringName(), piece.getSymbol(), piece.getColor().name());
    }

    @Override
    public Map<Position, Piece> load(int boardId) {
        Map<Position, Piece> board = new TreeMap<>();
        final List<Pair> pairs = getPairs(boardId);

        for (Pair pair : pairs) {
            board.put(pair.getPosition(), pair.getPiece());
        }
        return board;
    }

    private List<Pair> getPairs(int boardId) {
        String sql = "select position, type, color from piece where board_id = " + boardId;

        return jdbcTemplate.query(sql, (res, rowNum) -> {
            final Position position = Position.from(res.getString("position"));
            final Type type = Type.from(res.getString("type"));
            final Piece piece = type.makePiece(Color.from(res.getString("color")));
            return new Pair(position, piece);
        });
    }

    @Override
    public boolean existPieces(int boardId) {
        final String sql = "select count(*) from piece where board_id = ?";
        final Integer pieceCount = jdbcTemplate.queryForObject(sql, Integer.class, boardId);
        return !pieceCount.equals(0);
    }

    @Override
    public void delete(int boardId) {
        final String sql = "delete from piece where board_id = ?";
        jdbcTemplate.update(sql, boardId);
    }

    @Override
    public void updatePosition(String source, String target, int boardId) {
        final String type = getFromSource("type", source, boardId);
        final String color = getFromSource("color", source, boardId);

        final String updateSourceSql = "update piece set type = '.', color = 'NONE' where position = ? and board_id = ?";
        final String updateTargetSql = "update piece set type = ?, color = ? where position = ? and board_id = ?";

        jdbcTemplate.update(updateSourceSql, source, boardId);
        jdbcTemplate.update(updateTargetSql, type, color, target, boardId);
    }

    private String getFromSource(String column, String source, int boardId) {
        final String getFromSourceSql = "select " + column + " from piece where position = ? and board_id = ?";
        return jdbcTemplate.queryForObject(getFromSourceSql, String.class, source, boardId);
    }
}
