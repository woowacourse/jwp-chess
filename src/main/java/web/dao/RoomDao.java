package web.dao;

import java.sql.PreparedStatement;
import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import web.dto.RoomDto;

@Repository
public class RoomDao {

    private static final RowMapper<RoomDto> ROW_MAPPER = (rs, rn) -> {
        int id = rs.getInt("id");
        int chessGameId = rs.getInt("chess_game_id");
        String name = rs.getString("name");
        String password = rs.getString("password");
        return new RoomDto(id, chessGameId, name, password);
    };

    private final JdbcTemplate jdbcTemplate;

    public RoomDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<RoomDto> findAll() {
        return jdbcTemplate.query("SELECT id, chess_game_id, name, password FROM room", ROW_MAPPER);
    }

    public RoomDto findByName(String name) {
        try {
            return jdbcTemplate.queryForObject("SELECT id, chess_game_id, name, password FROM room WHERE name = ?",
                    ROW_MAPPER, name);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public RoomDto findById(int id) {
        try {
            return jdbcTemplate.queryForObject("SELECT id, chess_game_id, name, password FROM room WHERE id = ?",
                    ROW_MAPPER, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public RoomDto saveRoom(String name, String password, int chessGameId) {
        String sql = "INSERT INTO room(name, password, chess_game_id) VALUES(?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
                    ps.setString(1, name);
                    ps.setString(2, password);
                    ps.setInt(3, chessGameId);
                    return ps;
                }, keyHolder);
        return new RoomDto(keyHolder.getKey().intValue(), chessGameId, name, password);
    }

    public void deleteById(int id) {
        jdbcTemplate.update("DELETE FROM room WHERE id = ?", id);
    }
}
