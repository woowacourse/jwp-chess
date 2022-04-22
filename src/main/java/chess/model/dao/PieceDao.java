package chess.model.dao;

import chess.entity.PieceEntity;
import chess.model.board.Board;
import chess.model.piece.Piece;
import chess.model.position.Position;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

@Repository
public class PieceDao {
    private final JdbcTemplate jdbcTemplate;

    public PieceDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public static String getPieceName(Piece piece) {
        return (piece.getTeam().name() + "-" + piece.getName()).toLowerCase();
    }

    public void init(Board board) {
        String query = "insert into pieces (position, name) values (?, ?)";
        for (Map.Entry<Position, Piece> entry : board.getBoard().entrySet()) {
            jdbcTemplate.update(query, entry.getKey().getPosition(), getPieceName(entry.getValue()));
        }
    }

    public List<PieceEntity> findAllPieces() {
        String sql = "select position, name from pieces";
        return jdbcTemplate.query(
                sql,
                (resultSet, rowNum) -> {
                    PieceEntity pieceEntity = new PieceEntity(
                            resultSet.getString("name"),
                            resultSet.getString("position")
                    );
                    return pieceEntity;
                });
    }

    public String findPieceNameByPosition(String source) {
        String sql = "select name from pieces where position = (?)";
        return jdbcTemplate.queryForObject(sql, String.class, source);
    }

    public void updateByPosition(String position, String pieceName) {
        String sql = "UPDATE pieces SET name = (?) WHERE position = (?)";
        jdbcTemplate.update(sql, pieceName, position);
    }

    public void deleteAll() {
        String query = "DELETE FROM pieces";
        jdbcTemplate.update(query);
    }
}
