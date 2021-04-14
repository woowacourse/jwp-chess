package chess.dao;

import chess.domain.piece.Piece;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class SpringPieceDAO {

    private final JdbcTemplate jdbcTemplate;

    public SpringPieceDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long save(Long chessGameId, Piece piece) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String query = "INSERT INTO piece(color, shpe, chess_game_id, row, col) VALUES(?, ?, ?, ?, ?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, piece.getColor().toString());
            ps.setString(2, piece.getShape().toString());
            ps.setLong(3, chessGameId);
            ps.setInt(4, piece.getRow());
            ps.setInt(5, piece.getColumn());
            return ps;
        }, keyHolder);

        return (Long) keyHolder.getKey();
    }

    public void saveAll(final Long chessGameId, final List<Piece> pieces) {
        for (final Piece piece : pieces) {
            save(chessGameId, piece);
        }
    }

}
