package chess.dao;

import chess.domain.ChessGame2;
import chess.domain.Color;
import java.sql.PreparedStatement;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class BoardDaoImpl implements BoardDao{

    private final JdbcTemplate jdbcTemplate;

    public BoardDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Color findTurn(Long boardId) {
        final String sql = "select turn from board where id = ?";
        final String turn = jdbcTemplate.queryForObject(sql, String.class, boardId);

        return Color.from(turn);
    }

    @Override
    public void deleteBoard(Long boardId) {
        final String sql = "delete from board where id = ?";

        jdbcTemplate.update(sql, boardId);
    }

    @Override
    public boolean existsBoardByName(String title) {
        final String sql = "select count(*) from board where title = ?";
        final Integer numOfGame = jdbcTemplate.queryForObject(sql, Integer.class, title);
        return !numOfGame.equals(0);
    }

    @Override
    public Long save(ChessGame2 chessGame2) {
        final String sql = "insert into board (turn, title, password) values (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, chessGame2.getBoard().getTurn().name());
            ps.setString(2, chessGame2.getRoom().getTitle());
            ps.setString(3, chessGame2.getRoom().getPassword());
            return ps;
        }, keyHolder);
        return Long.valueOf(keyHolder.getKey().longValue());
    }

    @Override
    public void updateTurn(Long boardId, Color turn) {
        final String sql = "update board set turn = ? where id = ?";
        jdbcTemplate.update(sql, turn.name(), boardId);
    }
}
