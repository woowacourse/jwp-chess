package chess.web.dao;

import chess.board.piece.Empty;
import chess.board.piece.Piece;
import chess.board.piece.PieceFactory;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

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
    public void save(Piece piece, Long boardId) {
        String query = "INSERT INTO piece (position, board_id, type, team) VALUES (?, ?, ?, ?);";

        String position = piece.getPosition().name();
        String type = piece.getType();
        String team = piece.getTeam().value();
        jdbcTemplate.update(query, position, boardId, type, team);
    }

    @Override
    public void updatePieceByPositionAndBoardId(
            final String type,
            final String team,
            final String position,
            final Long boardId
    ) {
        String query = "UPDATE piece SET type =?, team =? WHERE position = ? AND board_id = ?";

        jdbcTemplate.update(query, type, team, position, boardId);
    }

    @Override
    public Optional<Piece> findByPositionAndBoardId(final String position, final Long boardId) {
        String query = "SELECT * FROM piece WHERE position = ? AND board_id = ?";

        try {
            List<Piece> findQuery = jdbcTemplate.query(query, piecesRowMapper, position, boardId);
            Piece piece = DataAccessUtils.nullableSingleResult(findQuery);
            return Optional.ofNullable(piece);
        } catch (DataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public List<Piece> findAllByBoardId(final Long boardId) {
        String query = "select position, team, type from piece WHERE board_id = ?";

        return jdbcTemplate.query(query, piecesRowMapper, boardId);
    }

    @Override
    public void save(List<Piece> pieces, Long boardId) {
        final String query = "INSERT INTO piece (position, board_id, type, team) VALUES ( ?, ?, ?, ?)";
        for (Piece piece : pieces) {
            jdbcTemplate.update(
                    query,
                    piece.getPosition().name(),
                    boardId,
                    piece.getType(),
                    piece.getTeam().value()
            );
        }
    }

    @Override
    public void deleteByBoardId(Long boardId) {
        final String query = "DELETE FROM piece WHERE board_id = ?";
        jdbcTemplate.update(query, boardId);
    }
}
