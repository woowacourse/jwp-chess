package chess.dao;

import chess.domain.location.Location;
import chess.domain.piece.Piece;
import chess.utils.PieceConverter;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class PieceDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Piece> pieceRowMapper = (resultSet, rowNum) -> PieceConverter
        .run(resultSet);


    public PieceDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insertAll(long gameId, List<Piece> pieces) {
        String sql = "INSERT INTO piece(game_id, x, y, color, shape) VALUES(?,?,?,?,?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Piece piece = pieces.get(i);
                ps.setLong(1, gameId);
                ps.setInt(2, piece.getX());
                ps.setInt(3, piece.getY());
                ps.setString(4, piece.getTeamValue());
                ps.setString(5, String.valueOf(piece.getPieceTypeValue()));
            }

            @Override
            public int getBatchSize() {
                return pieces.size();
            }
        });
    }

    public List<Piece> selectAll(long gameId) {
        String sql = "SELECT * FROM piece WHERE game_id = ?";
        return jdbcTemplate.query(sql, pieceRowMapper, gameId);
    }

    public void delete(long gameId, Location target) {
        String sql = "DELETE FROM piece WHERE game_id = ? AND x = ? AND y = ?";
        jdbcTemplate.update(sql, gameId, target.getX(), target.getY());
    }

    public void updatePosition(long gameId, Location source, Location target) {
        String sql = "UPDATE piece SET x = ?, y = ? WHERE game_id = ? AND x = ? AND y = ?";
        jdbcTemplate.update(sql, target.getX(), target.getY(), gameId, source.getX(),
            source.getY());
    }

    public void deletePieces(long gameId) {
        String sql = "DELETE FROM piece WHERE game_id = ?";
        jdbcTemplate.update(sql, gameId);
    }
}
