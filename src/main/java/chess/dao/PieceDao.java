package chess.dao;

import chess.dto.request.MoveRequestDto;
import chess.dto.response.ChessResponseDto;
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

    public void initializePieceStatus(final String pieceName, final String piecePosition, Long roomId) {
        String pieceName2 = pieceName;
        String piecePosition2 = piecePosition;
        String query = "INSERT INTO piece (piece_name, piece_position, room_id) VALUE (?, ?, ?)";
        jdbcTemplate.update(query, pieceName2, piecePosition2, roomId);
    }

    public List<ChessResponseDto> showAllPieces(final Long roomId) {
        List<ChessResponseDto> pieces;
        String query = "SELECT * FROM piece WHERE room_id = ?";
        pieces = jdbcTemplate.query(
                query, (rs, rowNum) -> new ChessResponseDto(
                        rs.getLong("id"),
                        rs.getString("piece_name"),
                        rs.getString("piece_position")), roomId
        );
        return pieces;
    }

    public void movePiece(final MoveRequestDto moveRequestDto) {
        String query = "UPDATE piece SET piece_position=? WHERE piece_position=?";
        jdbcTemplate.update(query, moveRequestDto.getTarget(), moveRequestDto.getSource());
    }

    public void removePiece(final MoveRequestDto moveRequestDto) {
        String query = "DELETE FROM piece WHERE piece_position=?";
        jdbcTemplate.update(query, moveRequestDto.getTarget());
    }
}
