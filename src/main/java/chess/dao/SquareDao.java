package chess.dao;

import chess.entity.SquareEntity;
import chess.model.board.Board;
import chess.model.piece.Piece;
import chess.model.position.Position;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class SquareDao {

    private JdbcTemplate jdbcTemplate;

    public SquareDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<SquareEntity> actorRowMapper = (resultSet, rowNum) -> {
        SquareEntity squareEntity = new SquareEntity(
                resultSet.getString("position"),
                resultSet.getString("team"),
                resultSet.getString("symbol")
        );
        return squareEntity;
    };

    public int insert(final String id, final Position position, final Piece piece) {
        final String sql = "insert into square (id, position, team, symbol) values (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, id, position.getKey(), piece.getTeam(), piece.getSymbol());
    }

    public Board createBoardFrom(final String id) {
        final List<SquareEntity> squareEntities = findSquaresFrom(id);
        final Map<Position, Piece> squares = new HashMap<>();
        for (SquareEntity squareEntity : squareEntities) {
            Position position = Position.from(squareEntity.getPosition());
            Piece piece = Piece.getPiece(squareEntity.getTeam() + "_" + squareEntity.getSymbol());
            squares.put(position, piece);
        }
        return Board.from(squares);
    }

    private List<SquareEntity> findSquaresFrom(final String id) {
        final String sql = "select position, team, symbol from square where id = ?";
        return jdbcTemplate.query(sql, actorRowMapper, id);
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
