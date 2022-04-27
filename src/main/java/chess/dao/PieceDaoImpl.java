package chess.dao;

import chess.domain.board.position.Position;
import chess.domain.piece.Piece;
import chess.dto.PieceDto;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class PieceDaoImpl implements PieceDao {

    private final JdbcTemplate jdbcTemplate;

    public PieceDaoImpl(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<PieceDto> actorRowMapper = (resultSet, rowNum) -> new PieceDto(
            resultSet.getString("name"),
            resultSet.getString("position"),
            resultSet.getString("teamColor")
    );

    @Override
    public List<PieceDto> findPiecesByRoomIndex(final int roomId) {
        final String sql = "select * from piece where roomId = ?";
        return jdbcTemplate.query(sql, actorRowMapper, roomId);
    }

    @Override
    public void saveAllPieces(final int roomId, final Map<Position, Piece> board) {
        final String sql = "insert into piece (roomId, name, position, teamColor) values (?, ?, ?, ?)";
        for (Position position : board.keySet()) {
            final Piece piece = board.get(position);
            jdbcTemplate.update(sql, roomId, piece.getName(), position.toString(), piece.getTeam());
        }
    }

    @Override
    public void removePiece(final int roomId, final String position) {
        final String sql = "delete from piece where roomId = ? and position = ?";
        jdbcTemplate.update(sql, roomId, position);
    }

    @Override
    public void savePiece(final int roomId, final String position, final Piece piece) {
        final String sql = "insert into piece (roomId, name, position, teamColor) values (?, ?, ?, ?)";
        jdbcTemplate.update(sql, roomId, piece.getName(), position, piece.getTeam());
    }

    @Override
    public void removeAllPieces(final int roomId) {
        final String sql = "delete from piece where roomId = ?";
        jdbcTemplate.update(sql, roomId);
    }
}
