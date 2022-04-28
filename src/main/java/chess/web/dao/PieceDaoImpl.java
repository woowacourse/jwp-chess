package chess.web.dao;

import chess.board.piece.Piece;
import chess.board.piece.PieceFactory;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class PieceDaoImpl implements PieceDao {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Piece> piecesRowMapper = (resultSet, rowNum) -> PieceFactory.create(
            resultSet.getString("position"),
            resultSet.getString("team"),
            resultSet.getString("type")
    );

    public PieceDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Piece piece, Long roomId) {
        String query = "INSERT INTO piece (position, room_id, type, team) VALUES (?, ?, ?, ?);";

        String position = piece.getPosition().name();
        String type = piece.getType();
        String team = piece.getTeam().value();
        jdbcTemplate.update(query, position, roomId, type, team);
    }

    @Override
    public void updatePieceByPositionAndRoomId(
            final String type,
            final String team,
            final String position,
            final Long roomId
    ) {
        String query = "UPDATE piece SET type =?, team =? WHERE position = ? AND room_id = ?";

        jdbcTemplate.update(query, type, team, position, roomId);
    }

    @Override
    public Optional<Piece> findByPositionAndRoomId(final String position, final Long roomId) {
        String query = "SELECT * FROM piece WHERE position = ? AND room_id = ?";

        try {
            List<Piece> findQuery = jdbcTemplate.query(query, piecesRowMapper, position, roomId);
            Piece piece = DataAccessUtils.nullableSingleResult(findQuery);
            return Optional.ofNullable(piece);
        } catch (DataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public List<Piece> findAllByRoomId(final Long roomId) {
        String query = "select position, team, type from piece WHERE room_id = ?";

        return jdbcTemplate.query(query, piecesRowMapper, roomId);
    }

    @Override
    public void save(List<Piece> pieces, Long roomId) {
        final String query = "INSERT INTO piece (position, room_id, type, team) VALUES ( ?, ?, ?, ?)";
        for (Piece piece : pieces) {
            jdbcTemplate.update(
                    query,
                    piece.getPosition().name(),
                    roomId,
                    piece.getType(),
                    piece.getTeam().value()
            );
        }
    }

    @Override
    public void deleteAllByRoomId(Long roomId) {
        final String query = "DELETE FROM piece WHERE room_id = ?";
        jdbcTemplate.update(query, roomId);
    }
}
