package chess.dao;

import chess.domain.Team;
import chess.entity.Room;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class RoomDaoImpl implements RoomDao {

    private JdbcTemplate jdbcTemplate;

    public RoomDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Room> findById(Long roomId) {
        final String sql = "select * from room  where id = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
            new Room(
                rs.getLong("id"),
                Team.valueOf(rs.getString("team")),
                rs.getString("title"),
                rs.getString("password"),
                rs.getBoolean("status")
            ), roomId));
    }

    @Override
    public void deleteBy(Long roomId, String password) {
        final String sql = "delete from room where id = ? and password = ?";
        jdbcTemplate.update(sql, roomId, password);
    }

    @Override
    public Long save(String title, String password) {
        final String sql = "insert into room (team, title, password, status) values(?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, Team.WHITE.name());
            ps.setString(2, title);
            ps.setString(3, password);
            ps.setBoolean(4, true);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    @Override
    public void updateTeam(Team team, Long roomId) {
        final String sql = "update room set team = ? where id = ?";
        jdbcTemplate.update(sql, team.name(), roomId);
    }

    @Override
    public List<Room> findAll() {
        final String sql = "select * from room";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Room(
            rs.getLong("id"),
            Team.valueOf(rs.getString("team")),
            rs.getString("title"),
            rs.getString("password"),
            rs.getBoolean("status")
        ));
    }

    @Override
    public void updateStatus(Long roomId, boolean status) {
        final String sql = "update room set status = ? where id = ?";
        jdbcTemplate.update(sql, status, roomId);
    }
}
