package chess.dao;

import chess.dto.BoardDto;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcTemplateBoardDao implements BoardDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<BoardDto> rowMapper;

    public JdbcTemplateBoardDao(JdbcTemplate jdbcTemplate, RowMapper<BoardDto> rowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = rowMapper;
    }

    @Override
    public void init(Map<String, String> board, int gameId) {
        String sql = "insert into board (position, piece, game_id) values (?, ? ,?)";

        for (Entry<String, String> boardEntry : board.entrySet()) {
            jdbcTemplate.update(sql, boardEntry.getKey(), boardEntry.getValue(), gameId);
        }
    }

    @Override
    public void update(String position, String piece, int gameId) {
        String sql = "update board set piece = ? where position = ? and game_id = ?";
        jdbcTemplate.update(sql, piece, position, gameId);
    }

    @Override
    public Map<String, String> getBoard(int gameId) {
        final String sql = "select * from board where game_id = ?";
        List<BoardDto> value = jdbcTemplate.query(sql, rowMapper, gameId);
        return value.stream().collect(Collectors.toMap(BoardDto::getPosition, BoardDto::getPiece));
    }

    @Override
    public void reset(Map<String, String> board, int gameId) {
        deleteById(gameId);
    }

    private void deleteById(int gameId) {
        String sql = "delete from board where game_id = ?";
        jdbcTemplate.update(sql, gameId);
    }
}
