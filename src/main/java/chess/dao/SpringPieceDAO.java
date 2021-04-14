package chess.dao;

import chess.domain.piece.Piece;
import chess.domain.piece.PieceFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class SpringPieceDAO {

    private final JdbcTemplate jdbcTemplate;

    public SpringPieceDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long save(Long chessGameId, Piece piece) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String query = "INSERT INTO piece(color, shape, chess_game_id, row, col) VALUES(?, ?, ?, ?, ?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, piece.getColor().toString());
            ps.setString(2, piece.getShape().toString());
            ps.setLong(3, chessGameId);
            ps.setInt(4, piece.getRow());
            ps.setInt(5, piece.getColumn());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public void saveAll(final Long chessGameId, final List<Piece> pieces) {
        for (final Piece piece : pieces) {
            save(chessGameId, piece);
        }
    }

    public List<Piece> findAllPiecesByChessGameId(Long chessGameId) {
        String query = "SELECT * FROM piece WHERE chess_game_id = ?";
        return jdbcTemplate.query(query, pieceRowMapper(), chessGameId);
    }

    public Optional<Piece> findOneByPosition(final Long chessGameId, final int row, final int col) {
        String query = "SELECT * FROM piece WHERE chess_game_id = ? AND row = ? AND col = ?";
        try {
            Piece piece = jdbcTemplate.queryForObject(query, pieceRowMapper(), chessGameId, row, col);
            return Optional.ofNullable(piece);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void update(final Piece piece) {
        String query = "UPDATE piece SET row = ?, col = ? WHERE id = ?";
        jdbcTemplate.update(query, piece.getRow(), piece.getColumn(), piece.getId());
    }

    public void delete(final Long chessGameId, final int row, final int col) {
        String query = "DELETE FROM piece WHERE chess_game_id = ? AND row = ? AND col = ?";
        jdbcTemplate.update(query, chessGameId, row, col);
    }

    private RowMapper<Piece> pieceRowMapper() {
        return (rs, rowNum) -> {
            long id = rs.getLong("id");
            String color = rs.getString("color");
            String shape = rs.getString("shape");
            int row = rs.getInt("row");
            int col = rs.getInt("col");
            Piece piece = PieceFactory.createPiece(shape, color, row, col);
            piece.setId(id);
            return piece;
        };
    }

}
