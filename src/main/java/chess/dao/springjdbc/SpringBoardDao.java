package chess.dao.springjdbc;

import chess.dao.BoardDao;
import chess.service.dto.BoardDto;
import chess.service.dto.PieceWithSquareDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class SpringBoardDao implements BoardDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<PieceWithSquareDto> pieceRowMapper = (resultSet, rowNum) ->
            new PieceWithSquareDto(
                    resultSet.getString("square"),
                    resultSet.getString("piece_type"),
                    resultSet.getString("piece_color")
            );

    public SpringBoardDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void initBoard(Long gameId) {
        String sql = "INSERT INTO board (game_id, piece_type, piece_color, square)\n"
            + "SELECT ?, init.piece_type, init.piece_color, init.square FROM init_board AS init";
        jdbcTemplate.update(sql, gameId);
    }

    @Override
    public BoardDto getBoardByGameId(Long gameId) {
        String sql = "SELECT piece_type, piece_color, square FROM board WHERE game_id = ?";
        return new BoardDto(jdbcTemplate.query(sql, pieceRowMapper, gameId));
    }

    @Override
    public void remove(Long id) {
        String sql = "DELETE FROM board WHERE game_id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void update(PieceWithSquareDto piece, Long gameId) {
        String sql = "UPDATE board SET piece_type = ?, piece_color = ? WHERE square = ? AND game_id = ?";
        jdbcTemplate.update(sql, piece.getType(), piece.getColor(), piece.getSquare(), gameId);
    }
}
