package chess.dao.springjdbc;

import chess.dao.PieceDao;
import chess.service.dto.BoardDto;
import chess.dao.PieceEntity;
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

    public SpringPieceDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void initBoard(int gameId) {
        String sql = "INSERT INTO piece (game_id, piece_type, piece_color, square)\n"
                + "SELECT ?, init.piece_type, init.piece_color, init.square FROM init_board AS init\n"
                + "ON duplicate KEY UPDATE piece_type = init.piece_type, piece_color = init.piece_color";
        jdbcTemplate.update(sql, gameId);
    }

    @Override
    public BoardDto getBoardByGameId(int id) {
        String sql = "SELECT piece_type, piece_color, square FROM piece WHERE game_id = ?";
        return new BoardDto(jdbcTemplate.query(sql, pieceRowMapper, id));
    }

    @Override
    public void remove(int id) {
        String sql = "DELETE FROM piece WHERE game_id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void update(PieceEntity piece, int gameId) {
        String sql = "UPDATE piece SET piece_type = ?, piece_color = ? WHERE square = ? AND game_id = ?";
        jdbcTemplate.update(sql, piece.getType(), piece.getColor(), piece.getSquare(), gameId);
    }
}
