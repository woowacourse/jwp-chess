package chess.dao;

import chess.domain.piece.Piece;
import chess.domain.piece.PieceFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class PieceDAO {

    private final JdbcTemplate jdbcTemplate;

    public PieceDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveAll(final Long chessGameId, final List<Piece> pieces) {
        String query = "INSERT INTO piece(color, shape, chess_game_id, row_index, col_index) VALUES(?, ?, ?, ?, ?)";
        List<Object[]> params = pieces.stream()
                .map(piece -> parseObjects(chessGameId, piece))
                .collect(Collectors.toList());
        jdbcTemplate.batchUpdate(query, params);
    }

    public List<Piece> findAllPiecesByChessGameId(Long chessGameId) {
        String query = "SELECT * FROM piece WHERE chess_game_id = ?";
        return jdbcTemplate.query(query, pieceRowMapper(), chessGameId);
    }

    public Optional<Piece> findOneByPosition(final Long chessGameId, final int row, final int col) {
        String query = "SELECT * FROM piece WHERE chess_game_id = ? AND row_index = ? AND col_index = ?";
        try {
            Piece piece = jdbcTemplate.queryForObject(query, pieceRowMapper(), chessGameId, row, col);
            return Optional.ofNullable(piece);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void update(final Piece piece) {
        String query = "UPDATE piece SET row_index = ?, col_index = ? WHERE id = ?";
        jdbcTemplate.update(query, piece.getRow(), piece.getColumn(), piece.getId());
    }

    public void delete(final Long chessGameId, final int row, final int col) {
        String query = "DELETE FROM piece WHERE chess_game_id = ? AND row_index = ? AND col_index = ?";
        jdbcTemplate.update(query, chessGameId, row, col);
    }

    private RowMapper<Piece> pieceRowMapper() {
        return (rs, rowNum) -> {
            long id = rs.getLong("id");
            String color = rs.getString("color");
            String shape = rs.getString("shape");
            int row = rs.getInt("row_index");
            int col = rs.getInt("col_index");
            Piece piece = PieceFactory.createPiece(shape, color, row, col);
            piece.setId(id);
            return piece;
        };
    }

    private Object[] parseObjects(Long chessGameId, Piece piece) {
        return new Object[]{piece.getColor().toString(), piece.getShape().toString(), chessGameId,
                piece.getRow(), piece.getColumn()};
    }

}
