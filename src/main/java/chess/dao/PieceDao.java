package chess.dao;

import chess.dao.dto.response.ChessResponseDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class PieceDao {
    private final JdbcTemplate jdbcTemplate;

    public PieceDao(final DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void initializePieceStatus(final String pieceName, final String piecePosition,
                                      final Long roomId) {
        String query = "INSERT INTO piece (piece_name, piece_position, room_id) VALUE (?, ?, ?)";
        jdbcTemplate.update(query, pieceName, piecePosition, roomId);
    }

    public List<ChessResponseDto> findAllPieces(final Long roomId) {
        String query = "SELECT * FROM piece WHERE room_id=?";
        return jdbcTemplate.query(
                query,
                (rs, rowNum) -> new ChessResponseDto(
                        rs.getLong("id"),
                        rs.getString("piece_name"),
                        rs.getString("piece_position")),
                roomId
        );
    }

    public void movePiece(final String source, final String target) {
        String query = "UPDATE piece SET piece_position=? WHERE piece_position=?";
        jdbcTemplate.update(query, target, source);
    }

    public void removePiece(final String target) {
        String query = "DELETE FROM piece WHERE piece_position=?";
        jdbcTemplate.update(query, target);
    }
}
