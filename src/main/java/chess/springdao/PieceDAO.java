package chess.springdao;

import chess.dao.ConnectionSetup;
import chess.domain.piece.Piece;
import chess.dto.GridDto;
import chess.dto.PieceDto;
import chess.dto.response.ResponseCode;
import chess.exception.ChessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PieceDAO {
    private JdbcTemplate jdbcTemplate;

    public PieceDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public long createPiece(long gridId, Piece piece) throws SQLException {
        boolean isBlack = piece.isBlack();
        String position = String.valueOf(piece.position().x()) + String.valueOf(piece.position().y());
        String name = String.valueOf(piece.name());
        String query = "INSERT INTO piece (isBlack, position, gridId, name) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query,
                    Statement.RETURN_GENERATED_KEYS);
            ps.setBoolean(1, isBlack);
            ps.setString(2, position);
            ps.setLong(3, gridId);
            ps.setString(4, name);
            return ps;
        }, keyHolder);
        return (long) keyHolder.getKey();
    }

    public List<PieceDto> findPiecesByGridId(long gridId) throws SQLException {
        String query = "SELECT * FROM piece WHERE gridId = ?";
        return jdbcTemplate.queryForObject(query,
                List.class, gridId);
    }

    public void updatePiece(long pieceId, boolean isBlack, char name) throws SQLException {
        String query = "UPDATE piece SET isBlack = ?, name = ?  WHERE pieceId = ?";
        jdbcTemplate.update(query, isBlack, String.valueOf(name), pieceId);
    }
}
