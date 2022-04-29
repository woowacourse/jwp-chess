package chess.dao;

import chess.domain.Color;
import chess.dto.BoardInfoDto;
import chess.dto.CreateBoardDto;
import java.sql.PreparedStatement;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class BoardJdbcTemplateDao implements BoardDao {

    private final JdbcTemplate jdbcTemplate;

    public BoardJdbcTemplateDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int makeBoard(Color turn, CreateBoardDto boardInfoDto) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(getBoardPreparedStatement(turn, boardInfoDto), keyHolder);

        return keyHolder.getKey().intValue();
    }

    private PreparedStatementCreator getBoardPreparedStatement(Color turn, CreateBoardDto boardInfoDto) {
        String sql = "insert into board (turn, name, password) values (?, ?, ?)";

        return connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, turn.name());
            ps.setString(2, boardInfoDto.getName());
            ps.setString(3, boardInfoDto.getPassword());
            return ps;
        };
    }

    public List<BoardInfoDto> getAllBoardInfo() {
        String sql = "select id, name from board";
        return jdbcTemplate.query(sql, (res, rowNum) -> new BoardInfoDto(res.getInt("id"), res.getString("name")));
    }

    @Override
    public void updateTurn(Color turn, int id) {
        String sql = "update board set turn = ? where id = ?";

        jdbcTemplate.update(sql, turn.name(), id);
    }

    @Override
    public boolean isGameEnd(int id) {
        final String sql = "select turn from board where id = ?";
        Color turn = jdbcTemplate.queryForObject(sql, Color.class, id);
        return turn == Color.END;
    }


    @Override
    public Color findTurn(int id) {
        final String sql = "select turn from board where id = ?";
        final String turn = jdbcTemplate.queryForObject(sql, String.class, id);

        return Color.from(turn);
    }


    @Override
    public void end(int id) {
        final String sql = "update board set turn = ? where id = ?";

        jdbcTemplate.update(sql, Color.END.name(), id);
    }

    @Override
    public String getPassword(int id) {
        final String sql = "select password from board where id = ?";

        return jdbcTemplate.queryForObject(sql, String.class, id);
    }

    @Override
    public String getName(int id) {
        final String sql = "select name from board where id = ?";

        return jdbcTemplate.queryForObject(sql, String.class, id);
    }

    @Override
    public void deleteBoard(int id, String password) {
        final String sql = "delete from board where id = ? and password = ?";

        jdbcTemplate.update(sql, id, password);
    }
}
