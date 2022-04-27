package chess.dao;

import chess.domain.Team;
import chess.entity.Room;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RoomDaoImpl implements RoomDao {

    private JdbcTemplate jdbcTemplate;

    public RoomDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Room findById(Long roomId) {
        final String sql = "select * from room  where id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                new Room(
                    rs.getLong("id"),
                    Team.valueOf(rs.getString("team")),
                    "이름",
                    "비밀번호",
                    true
                ), roomId);
        } catch (Exception exception) {
            return null;
        }
    }

    @Override
    public void deleteBy(Long roomId, String password) {
        final String sql = "delete from room where id = ? and password = ?";
        jdbcTemplate.update(sql, roomId, password);
    }

    @Override
    public Long save(Room room) {
        final String sql = "insert into room (team, title, password, status) values(?, ?, ?, ?)";
        return (long) jdbcTemplate.update(sql, room.getTeam().name(), room.getTitle(),
            room.getPassword(), room.getStatus());
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
    public void updateStatus(Long roomId) {
        final String sql = "update room set status = ? where id = ?";
        jdbcTemplate.update(sql, false, roomId);
    }
}
