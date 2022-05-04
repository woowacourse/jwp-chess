package chess.dao;

import chess.entity.Square;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class BoardDaoImpl implements BoardDao {

    private JdbcTemplate jdbcTemplate;

    private RowMapper<Square> rowMapper = (rs, rowNum) ->
            new Square(
                    rs.getString("position"),
                    rs.getString("symbol"),
                    rs.getString("color")
            );

    public BoardDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(List<Square> squares) {
        String sql = "insert into board (position, symbol, color, game_id) values (?, ?, ?, ?)";

        List<Object[]> board = squares.stream()
                .map(square -> new Object[]{
                        square.getPosition(),
                        square.getSymbol(),
                        square.getColor(),
                        square.getGameId()})
                .collect(Collectors.toList());

        jdbcTemplate.batchUpdate(sql, board);
    }

    @Override
    public List<Square> findById(int id) {
        String sql = "select position, symbol, color from board where game_id = ?";
        return jdbcTemplate.query(sql, rowMapper, id);
    }

    @Override
    public int update(Square square) {
        String sql = "update board set symbol = ?, color = ? where game_id = ? and position = ?";
        return jdbcTemplate.update(sql, square.getSymbol(), square.getColor(), square.getGameId(),
                square.getPosition());
    }

    @Override
    public void delete(int gameId) {
        String sql = "delete from board where game_id = ?";
        jdbcTemplate.update(sql, gameId);
    }
}
