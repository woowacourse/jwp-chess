package chess.dao;

import chess.domain.Color;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BoardJdbcTemplateDao implements BoardDao{

    private JdbcTemplate jdbcTemplate;

    public BoardJdbcTemplateDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Color turn) {
        final String sql = decideSql();

        jdbcTemplate.update(sql, turn.name());
    }

    private String decideSql() {
        if (existBoard()) {
            return "update board set turn = ?";
        }
        return "insert into board (id, turn) values (1, ?)";
    }

    private boolean existBoard() {
        final String sql = "select count(*) from board where id = 1";
        final Integer boardCount = jdbcTemplate.queryForObject(sql, Integer.class);
        return !boardCount.equals(0);
    }


    @Override
    public Color findTurn() {
        final String sql = "select turn from board";
        final String turn = jdbcTemplate.queryForObject(sql, String.class);

        return Color.from(turn);
    }

    @Override
    public void deleteBoard() {
        final String sql = "delete from board where id = 1";

        jdbcTemplate.update(sql);
    }
}
