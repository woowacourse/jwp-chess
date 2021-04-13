package chess.dao.piece;

import chess.dao.entity.PieceEntity;
import chess.domain.piece.Piece;
import chess.domain.piece.type.PieceType;
import chess.domain.player.type.TeamColor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PieceDAO implements PieceRepository {
    private final JdbcTemplate jdbcTemplate;

    public PieceDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Piece findByPieceTypeAndTeamColor(PieceType pieceType, TeamColor teamColor) {
        String query = "SELECT * FROM piece WHERE name = ? AND color = ?";
        return jdbcTemplate.queryForObject(
            query,
            (resultSet, rowNum) -> Piece.of(new PieceEntity(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("color"))
            ), pieceType.name(), teamColor.getValue());
    }
}
