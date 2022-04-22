package chess.dao.springjdbc;

import chess.dao.BoardDao;
import chess.dao.jdbc.jdbcutil.JdbcUtil;
import chess.dao.jdbc.jdbcutil.StatementExecutor;
import chess.service.dto.BoardDto;
import chess.service.dto.PieceWithSquareDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class DatabaseBoardDao2 implements BoardDao {

    private JdbcTemplate jdbcTemplate;
    private final RowMapper<PieceWithSquareDto> pieceRowMapper = (resultSet, rowNum) ->
            new PieceWithSquareDto(
                    resultSet.getString("square"),
                    resultSet.getString("piece_type"),
                    resultSet.getString("piece_color")
            );
    public DatabaseBoardDao2(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void initBoard(int gameId) {
        String sql = "INSERT INTO board (game_id, piece_type, piece_color, square)\n"
                + "SELECT ?, init.piece_type, init.piece_color, init.square FROM init_board AS init\n"
                + "ON duplicate KEY UPDATE piece_type = init.piece_type, piece_color = init.piece_color";
        jdbcTemplate.update(sql, gameId);
    }

    @Override
    public BoardDto getBoardByGameId(int id) {
        String sql = "SELECT piece_type, piece_color, square FROM board WHERE game_id = ?";
        return new BoardDto(jdbcTemplate.query(sql, pieceRowMapper, id));
    }

    @Override
    public void remove(int id) {
        String sql = "DELETE FROM board WHERE game_id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void update(PieceWithSquareDto piece, int gameId) {
        String sql = "UPDATE board SET piece_type = ?, piece_color = ? WHERE square = ? AND game_id = ?";
        jdbcTemplate.update(sql, piece.getType(), piece.getColor(), piece.getSquare(), gameId);
    }
}
