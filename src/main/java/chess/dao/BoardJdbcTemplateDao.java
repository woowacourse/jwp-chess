package chess.dao;

import chess.domain.Color;
import chess.domain.board.Position;
import chess.domain.piece.Piece;
import chess.domain.piece.Type;
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
public class BoardJdbcTemplateDao implements BoardDao{

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
        String sql = "insert into board (id, turn) values (?, ?)";

        jdbcTemplate.update(sql, id, turn.name());
    }

    @Override
    public boolean existBoard(int id) {
        final String sql = "select count(*) from board where id = ?";
        final Integer boardCount = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return !boardCount.equals(0);
    }


    @Override
    public Color findTurn(int id) {
        final String sql = "select turn from board where id = ?";
        final String turn = jdbcTemplate.queryForObject(sql, String.class, id);

        return Color.from(turn);
    }

    @Override
    public void deleteBoard(int id) {
        final String sql = "delete from board where id = ?";

        jdbcTemplate.update(sql, id);
    }
}
