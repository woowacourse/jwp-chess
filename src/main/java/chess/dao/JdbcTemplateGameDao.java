package chess.dao;

import chess.dto.GameDto;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class JdbcTemplateGameDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateGameDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

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

    public List<GameDto> find() {
        String sql = "select * from game";
        return jdbcTemplate.query(sql, new RowMapper<GameDto>() {
            @Override
            public GameDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new GameDto(rs.getString("title"), rs.getString("password"));
            }
        });
    }

    public void delete(int id) {
        String sql = "delete from game where id = ?";
        jdbcTemplate.update(sql, id);
    }
}
