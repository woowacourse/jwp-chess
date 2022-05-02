package chess.repository.dao.springjdbc;

import chess.repository.dao.PieceDao;
import chess.repository.dao.entity.PieceEntity;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class SpringPieceDao implements PieceDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<PieceEntity> pieceRowMapper = (resultSet, rowNum) ->
            new PieceEntity(
                    resultSet.getInt("id"),
                    resultSet.getInt("game_id"),
                    resultSet.getString("square"),
                    resultSet.getString("piece_type"),
                    resultSet.getString("piece_color")
            );

    public SpringPieceDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void initBoard(final Integer gameId) {
        String sql = "INSERT INTO piece (game_id, piece_type, piece_color, square)"
                + " SELECT ?, init.piece_type, init.piece_color, init.square FROM init_board AS init"
                + " ON duplicate KEY UPDATE piece_type = init.piece_type, piece_color = init.piece_color";
        jdbcTemplate.update(sql, gameId);
    }

    @Override
    public List<PieceEntity> getBoardByGameId(final Integer id) {
        String sql = "SELECT * FROM piece WHERE game_id = ?";
        return jdbcTemplate.query(sql, pieceRowMapper, id);
    }

    @Override
    public int update(final PieceEntity piece) {
        String sql = "UPDATE piece SET piece_type = ?, piece_color = ? WHERE square = ? AND game_id = ?";
        return jdbcTemplate.update(sql, piece.getType(), piece.getColor(), piece.getSquare(), piece.getGameId());
    }
}
