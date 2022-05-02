package chess.dao;

import chess.domain.piece.Piece;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ChessPieceDao {

    private final JdbcTemplate jdbcTemplate;

    public ChessPieceDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveAll(final Long gameId, List<Piece> pieces) {
        final String sql = "insert into "
                + "Piece(square_file, square_rank, team, piece_type, game_id) "
                + "values (?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(final PreparedStatement ps, final int i) throws SQLException {
                ps.setString(1, String.valueOf(pieces.get(i).getSquare().getFile().getValue()));
                ps.setString(2, String.valueOf(pieces.get(i).getSquare().getRank().getValue()));
                ps.setString(3, pieces.get(i).getTeam().name());
                ps.setString(4, pieces.get(i).getPieceType().name());
                ps.setLong(5, gameId);
            }

            @Override
            public int getBatchSize() {
                return pieces.size();
            }
        });
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
