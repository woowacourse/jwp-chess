package chess.dao;

import chess.dto.GameDto;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcTemplateGameDao implements GameDao {

    private static final int DELETE_SUCCESS_VALUE = 1;

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<GameDto> rowMapper;

    public JdbcTemplateGameDao(JdbcTemplate jdbcTemplate, RowMapper<GameDto> rowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = rowMapper;
    }

    @Override
    public int create(String roomTitle, String password) {
        String sql = "insert into game (title, password) values (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, roomTitle);
            ps.setString(2, password);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    @Override
    public List<GameDto> find() {
        String sql = "select * from game";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public boolean delete(int id, String password) {
        String sql = "delete from game where id = ? and password = ?";
        return jdbcTemplate.update(sql, id, password) == DELETE_SUCCESS_VALUE;
    }
}
