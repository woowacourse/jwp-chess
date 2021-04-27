package chess.dao;

import chess.dao.dto.PieceDto;
import chess.domain.location.Location;
import chess.domain.piece.Piece;
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
    private final RowMapper<PieceDto> pieceRowMapper = (resultSet, rowNum) -> new PieceDto(
        resultSet.getLong("id"),
        resultSet.getLong("game_id"),
        resultSet.getInt("x"),
        resultSet.getInt("y"),
        resultSet.getString("color"),
        resultSet.getString("shape").charAt(0)
    );

    public PieceDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insertAll(final long gameId, final List<Piece> pieces) {
        final String sql = "INSERT INTO piece(game_id, x, y, color, shape) VALUES(?,?,?,?,?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(final PreparedStatement ps, final int i) throws SQLException {
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

    public List<PieceDto> selectAll(final long gameId) {
        final String sql = "SELECT * FROM piece WHERE game_id = ?";
        return jdbcTemplate.query(sql, pieceRowMapper, gameId);
    }

    public void deleteBatchByIds(final List<Long> ids) {
        final String sql = "DELETE FROM piece WHERE id = ?";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(final PreparedStatement ps, final int i) throws SQLException {
                ps.setLong(1, ids.get(i));
            }

            @Override
            public int getBatchSize() {
                return ids.size();
            }
        });
    }

    public void deleteByLocation(final long gameId, final Location target) {
        final String sql = "DELETE FROM piece WHERE game_id = ? AND x = ? AND y = ?";
        jdbcTemplate.update(sql, gameId, target.getX(), target.getY());
    }

    public void updatePosition(final long gameId, Location source, final Location target) {
        final String sql = "UPDATE piece SET x = ?, y = ? WHERE game_id = ? AND x = ? AND y = ?";
        jdbcTemplate.update(sql, target.getX(), target.getY(), gameId, source.getX(),
            source.getY());
    }

    public void deletePiecesByGameId(final long gameId) {
        final String sql = "DELETE FROM piece WHERE game_id = ?";
        jdbcTemplate.update(sql, gameId);
    }

}
