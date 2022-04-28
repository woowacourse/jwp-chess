package chess.dao;

import chess.domain.piece.Piece;
import chess.domain.piece.PieceFactory;
import chess.domain.piece.Pieces;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class PieceDao {

    private final JdbcTemplate jdbcTemplate;

    public PieceDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createAll(List<Piece> pieces, String gameId) {
        final String sql = "insert into piece (name, color, position, game_id) values (?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, pieces, pieces.size(),
                (statement, piece) -> {
                    statement.setString(1, piece.getName());
                    statement.setString(2, piece.getColor().getName());
                    statement.setString(3, piece.getPosition().getPosition());
                    statement.setString(4, gameId);
                }
        );
    }

    public void updateAll(List<Piece> pieces, String  gameId) {
        final String sql = "UPDATE piece SET position = ? "
                + "WHERE game_id = ? "
                + "AND name = ? "
                + "AND color = ?";

        jdbcTemplate.batchUpdate(sql, pieces, pieces.size(),
                (statement, piece) -> {
                    statement.setString(1, piece.getPosition().getPosition());
                    statement.setString(2, gameId);
                    statement.setString(3, piece.getName());
                    statement.setString(4, piece.getColor().getName());
                }
        );
    }

    public Pieces findAll(String gameId) {
        final String sql = "select name, color, position from piece where game_id = ?";

        List<Piece> pieces = jdbcTemplate.query(sql, (resultSet, rowNum) -> PieceFactory.of(
                resultSet.getString("name"),
                resultSet.getString("color"),
                resultSet.getString("position")
        ), gameId);

        return new Pieces(pieces);
    }

    public void deleteAll(String gameId) {
        final String sql = "delete from piece where game_id = ?";
        jdbcTemplate.update(sql, gameId);
    }
}
