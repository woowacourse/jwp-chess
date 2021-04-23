package chess.dao;

import chess.dto.request.MoveRequestDto;
import chess.dto.response.ChessResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PieceDao {
    private final JdbcTemplate jdbcTemplate;

    public PieceDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void initializePieceStatus(final String pieceName, final String piecePosition) {
        String query = "INSERT INTO piece (piece_name, piece_position) VALUE (?, ?)";
        try {
            jdbcTemplate.update(query, pieceName, piecePosition);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<ChessResponseDto> showAllPieces() {
        List<ChessResponseDto> pieces = new ArrayList<>();
        String query = "SELECT * FROM piece";

        try {
            pieces = jdbcTemplate.query(
                    query, (rs, rowNum) -> new ChessResponseDto(
                            rs.getLong("id"),
                            rs.getString("piece_name"),
                            rs.getString("piece_position"))
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pieces;
    }

    public void movePiece(final MoveRequestDto moveRequestDto) {
        String query = "UPDATE piece SET piece_position=? WHERE piece_position=?";
        try {
            jdbcTemplate.update(query, moveRequestDto.getTarget(), moveRequestDto.getSource());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeAllPieces() {
        String query = "DELETE FROM piece";
        try {
            jdbcTemplate.update(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removePiece(final MoveRequestDto moveRequestDto) {
        String query = "DELETE FROM piece WHERE piece_position=?";
        try {
            jdbcTemplate.update(query, moveRequestDto.getTarget());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
