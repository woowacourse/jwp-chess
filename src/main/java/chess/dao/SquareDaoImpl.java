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
public class SquareDaoImpl implements SquareDao {

    private JdbcTemplate jdbcTemplate;

    public SquareDaoImpl(JdbcTemplate jdbcTemplate) {
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

    public void insert(Position position, Piece piece) {
        final String sql = "insert into square (position, team, symbol) values (?, ?, ?)";
        jdbcTemplate.update(sql, position.getKey(), piece.getTeam(), piece.getSymbol());
    }

    public Board createBoard() {
        List<SquareEntity> squareEntities = findAll();
        Map<Position, Piece> squares = new HashMap<>();
        for (SquareEntity squareEntity : squareEntities) {
            Position position = Position.from(squareEntity.getPosition());
            Piece piece = Piece.getPiece(squareEntity.getTeam() + "_" + squareEntity.getSymbol());
            squares.put(position, piece);
        }
        return Board.from(squares);
    }

    private List<SquareEntity> findAll() {
        final String sql = "select position, team, symbol from square";
        return jdbcTemplate.query(sql, actorRowMapper);
    }

    public int delete() {
        final String sql = "delete from square";
        return jdbcTemplate.update(sql);
    }

    public int update(Position position, Piece piece) {
        final String sql = "update square set team = ?, symbol = ? where position = ?";
        return jdbcTemplate.update(sql, piece.getTeam(), piece.getSymbol(), position.getKey());
    }
}
