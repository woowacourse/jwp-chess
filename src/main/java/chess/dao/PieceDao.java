package chess.dao;

import chess.domain.piece.Piece;
import chess.domain.piece.PieceFactory;
import chess.domain.piece.Pieces;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PieceDao {

    private JdbcTemplate jdbcTemplate;

    public PieceDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createAllById(List<Piece> pieces, String gameId) {
        final String sql = "insert into piece (name, color, position, game_id) values (?, ?, ?, ?)";

        for (Piece piece : pieces) {
            jdbcTemplate.update(sql,
                piece.getName(),
                piece.getColor().getName(),
                piece.getPosition().getPosition(),
                gameId);
        }
    }

    public void updateAllByGameId(List<Piece> pieces, String gameId) {
        final String sql = "UPDATE piece SET position = ? "
                + "WHERE game_id = ? "
                + "AND name = ? "
                + "AND color = ?";

        for (Piece piece : pieces) {
            jdbcTemplate.update(sql,
                piece.getPosition().getPosition(),
                gameId,
                piece.getName(),
                piece.getColor().getName());
        }
    }

    public Pieces findAllByGameId(String gameId) {
        final String sql = "select name, color, position from piece where game_id = ?";

        List<Piece> pieces = jdbcTemplate.query(sql, (resultSet, rowNum) -> {
            Piece piece = PieceFactory.of(
                resultSet.getString("name"),
                resultSet.getString("color"),
                resultSet.getString("position")
            );
            return piece;
        }, gameId);

        return new Pieces(pieces);
    }

    public void deleteAllByGameId(String gameId) {
        final String sql = "delete from piece where game_id = ?";
        jdbcTemplate.update(sql, gameId);
    }
}
