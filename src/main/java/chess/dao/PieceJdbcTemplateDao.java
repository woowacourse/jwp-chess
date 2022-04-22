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
public class PieceJdbcTemplateDao implements PieceDao{
    private JdbcTemplate jdbcTemplate;

    public PieceJdbcTemplateDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Map<Position, Piece> board) {
        for (Entry<Position, Piece> positionPieceEntry : board.entrySet()) {
            executeInsertPiece(positionPieceEntry);
        }
    }

    private void executeInsertPiece(Entry<Position, Piece> positionPieceEntry) {
        final String sql = "insert into piece (board_id, position, type, color) values (1, ?, ?, ?)";
        final Position position = positionPieceEntry.getKey();
        final Piece piece = positionPieceEntry.getValue();
        jdbcTemplate.update(sql, position.stringName(), piece.getSymbol(), piece.getColor().name());
    }

    @Override
    public Map<Position, Piece> load() {
        final String sql = "select position, type, color from piece where board_id = 1";
        Map<Position, Piece>  board = new TreeMap<>();

        final List<Pair> query = jdbcTemplate.query(sql, (res, rowNum) -> {
            final Position position = Position.from(res.getString("position"));
            final Type type = Type.from(res.getString("type"));
            final Piece piece = type.makePiece(Color.from(res.getString("color")));
            return new Pair(position, piece);
        });

        for (Pair pair : query) {
            board.put(pair.getPosition(), pair.getPiece());
        }
        return board;
    }

    @Override
    public boolean existPieces() {
        final String sql = "select count(*) from piece where board_id = 1";
        final Integer integer = jdbcTemplate.queryForObject(sql, Integer.class);
        return !integer.equals(0);
    }

    @Override
    public void delete() {
        final String sql = "delete from piece where board_id = 1";
        jdbcTemplate.update(sql);
    }

    @Override
    public void updatePosition(String source, String target) {
        final String getTypeFromSourceSql = "select type from piece where position = ? and board_id = 1";
        final String getColorFromSourceSql = "select color from piece where position = ? and board_id = 1";
        final String updateSourceSql = "update piece set type = '.', color = 'NONE' where position = ? and board_id = 1";
        final String updateTargetSql = "update piece set type = ?, color = ? where position = ? and board_id = 1";
        final String type = jdbcTemplate.queryForObject(getTypeFromSourceSql, String.class, source);
        final String color = jdbcTemplate.queryForObject(getColorFromSourceSql, String.class, source);

        jdbcTemplate.update(updateSourceSql, source);
        jdbcTemplate.update(updateTargetSql, type, color, target);
    }
}
