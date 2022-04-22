package chess.web.dao;

import chess.board.piece.Piece;
import chess.board.piece.PieceFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PieceDaoImpl implements PieceDao {

    private final JdbcTemplate jdbcTemplate;

    public PieceDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Piece> piecesRowMapper = (resultSet, rowNum) -> PieceFactory.create(
            resultSet.getString("position"),
            resultSet.getString("team"),
            resultSet.getString("type")
    );

    @Override
    public void updatePieceByPositionAndBoardId(final String type, final String team, final String position, final Long boardId) {
        String query = "update piece set type =?, team =? where position = ? AND board_id = ?";

        jdbcTemplate.update(query, type, team, position, boardId);
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
