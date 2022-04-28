package chess.dao;

import chess.dto.BoardDto;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcTemplateBoardDao implements BoardDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateBoardDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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
        List<BoardDto> value = jdbcTemplate.query(sql, new BoardMapper(), gameId);
        return value.stream().collect(Collectors.toMap(BoardDto::getPosition, BoardDto::getPiece));
    }

    @Override
    public void reset(Map<String, String> board) {
        removeAll();
    }

    private void removeAll() {
        String sql = "truncate table board";
        jdbcTemplate.update(sql);
    }
}
