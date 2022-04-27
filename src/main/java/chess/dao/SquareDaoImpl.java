package chess.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import chess.entity.SquareEntity;
import chess.model.board.Board;
import chess.model.piece.Piece;
import chess.model.position.Position;

@Repository
public class SquareDaoImpl implements SquareDao {

    private JdbcTemplate jdbcTemplate;

    public SquareDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<SquareEntity> squareEntityRowMapper = (resultSet, rowNum) -> new SquareEntity(
        resultSet.getString("position"),
        resultSet.getString("team"),
        resultSet.getString("symbol")
    );

    public void insert(Long id, Position position, Piece piece) {
        final String sql = "insert into square (chess_id, position, team, symbol) values (?, ?, ?, ?)";
        jdbcTemplate.update(sql, id, position.getKey(), piece.getTeam(), piece.getSymbol());
    }

    public Board createBoard(Long id) {
        List<SquareEntity> squareEntities = findAll(id);
        Map<Position, Piece> squares = new HashMap<>();
        for (SquareEntity squareEntity : squareEntities) {
            Position position = Position.from(squareEntity.getPosition());
            Piece piece = Piece.getPiece(squareEntity.getTeam() + "_" + squareEntity.getSymbol());
            squares.put(position, piece);
        }
        return Board.from(squares);
    }

    private List<SquareEntity> findAll(Long id) {
        final String sql = "select position, team, symbol from square where chess_id = ?";
        return jdbcTemplate.query(sql, squareEntityRowMapper, id);
    }

    public int delete(Long id) {
        final String sql = "delete from square where chess_id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public int update(Long id, Position position, Piece piece) {
        final String sql = "update square set team = ?, symbol = ? where chess_id = ? and position = ?";
        return jdbcTemplate.update(sql, piece.getTeam(), piece.getSymbol(), id, position.getKey());
    }
}
