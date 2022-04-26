package chess.dao;

import chess.domain.piece.Piece;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ChessPieceDao {

    private final JdbcTemplate jdbcTemplate;

    public ChessPieceDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Long gameId, Piece piece) {
        final String sql = "insert into "
                + "Piece(square_file, square_rank, team, piece_type, game_id) "
                + "values (?, ?, ?, ?, ?);";
        jdbcTemplate.update(sql,
                String.valueOf(piece.getSquare().getFile().getValue()),
                String.valueOf(piece.getSquare().getRank().getValue()),
                piece.getTeam().name(),
                piece.getPieceType().name(),
                gameId
        );
    }
}
