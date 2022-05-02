package chess.web.dao;

import chess.domain.board.piece.Piece;
import chess.domain.board.piece.PieceFactory;
import org.springframework.dao.DataAccessException;
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
    public void updatePieceByPositionAndBoardId(final String type, final String team, final String position, final Long boardId) {
        String query = "update piece set type =?, team =? where position = ? AND room_id = ?";

        jdbcTemplate.update(query, type, team, position, boardId);
    }

    @Override
    public List<Piece> findAllByBoardId(final Long boardId) {
        String query = "select position, team, type from piece WHERE room_id = ?";

        return jdbcTemplate.query(query, piecesRowMapper, boardId);
    }

    @Override
    public void save(List<Piece> pieces, Long boardId) {
        final String query = "INSERT INTO piece (position, room_id, type, team) VALUES ( ?, ?, ?, ?)";
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
        final String query = "DELETE FROM piece WHERE room_id = ?";
        try {
            jdbcTemplate.update(query, boardId);
        } catch (DataAccessException e) {
            throw new RuntimeException("체스 기물들 삭제에 실패했습니다.");
        }
    }
}
