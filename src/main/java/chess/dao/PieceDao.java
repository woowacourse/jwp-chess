package chess.dao;

import chess.dto.request.PieceDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PieceDao {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public PieceDao(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public void insertInitialPieces(int roomId, final String pieceName, final String piecePosition) {
        String query = "INSERT INTO piece (piece_name, piece_position, room_id) VALUE (?, ?, ?)";
        jdbcTemplate.update(query, pieceName, piecePosition, roomId);
    }

    public void movePiece(String source, String target, int roomId) {
        String query = "UPDATE piece SET piece_position=? WHERE piece_position=? AND room_id = ?";
        jdbcTemplate.update(query, target, source, roomId);
    }

    public void removePiece(String target, int roomId) {
        String query = "DELETE FROM piece WHERE piece_position=? AND room_id = ?";
        jdbcTemplate.update(query, target, roomId);
    }

    public List<PieceDto> getPieces(int roomId) {
        String query = "SELECT * FROM piece WHERE room_id = :room_id";
        SqlParameterSource namedParameters = new MapSqlParameterSource("room_id", roomId);
        List<PieceDto> pieces = this.namedParameterJdbcTemplate.query(
                query,
                namedParameters,
                (rs, rowNum) -> new PieceDto(
                        rs.getString("piece_name"),
                        rs.getString("piece_position"))
        );
        return pieces;
    }
}
