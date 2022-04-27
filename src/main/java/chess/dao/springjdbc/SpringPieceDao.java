package chess.dao.springjdbc;

import chess.dao.PieceDao;
import chess.dao.PieceEntity;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class SpringPieceDao implements PieceDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<PieceEntity> pieceRowMapper = (resultSet, rowNum) ->
            new PieceEntity(
                    resultSet.getString("square"),
                    resultSet.getString("piece_type"),
                    resultSet.getString("piece_color")
            );

    public SpringPieceDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void initBoard(final int gameId) {
        String sql = "INSERT INTO piece (game_id, piece_type, piece_color, square)"
                + " SELECT ?, init.piece_type, init.piece_color, init.square FROM init_board AS init"
                + " ON duplicate KEY UPDATE piece_type = init.piece_type, piece_color = init.piece_color";
        jdbcTemplate.update(sql, gameId);
    }

    @Override
    public List<PieceEntity> getBoardByGameId(final int id) {
        String sql = "SELECT piece_type, piece_color, square FROM piece WHERE game_id = ?";
        return jdbcTemplate.query(sql, pieceRowMapper, id);
    }

    @Override
    public void remove(final int id) {
        String sql = "DELETE FROM piece WHERE game_id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public int update(final PieceEntity piece, final int gameId) {
        String sql = "UPDATE piece SET piece_type = ?, piece_color = ? WHERE square = ? AND game_id = ?";
        return jdbcTemplate.update(sql, piece.getType(), piece.getColor(), piece.getSquare(), gameId);
    }
}
