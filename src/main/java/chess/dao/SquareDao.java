package chess.dao;

import chess.model.piece.Piece;
import chess.model.position.Position;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SquareDao {

    private JdbcTemplate jdbcTemplate;

    public SquareDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int insert(final String id, final Position position, final Piece piece) {
        final String sql = "insert into square (id, position, team, symbol) values (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, id, position.getKey(), piece.getTeam(), piece.getSymbol());
    }

    public Map<Position, Piece> findSquaresFrom(final String id) {
        final String sql = "select position, team, symbol from square where id = ?";
        List<Map<String, Object>> squares = jdbcTemplate.queryForList(sql, id);
        return squares.stream()
                .collect(Collectors.toMap(k -> Position.from((String) k.get("position")),
                        k -> Piece.getPiece((String) k.get("team"), (String) k.get("symbol"))));
    }

    public int deleteFrom(final String id) {
        final String sql = "delete from square where id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public int update(final String id, final Position position, final Piece piece) {
        final String sql = "update square set team = ?, symbol = ? where id = ? AND position = ?";
        return jdbcTemplate.update(sql, piece.getTeam(), piece.getSymbol(), id, position.getKey());
    }
}
