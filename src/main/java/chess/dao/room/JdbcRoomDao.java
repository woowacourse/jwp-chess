package chess.dao.room;

import chess.domain.game.Room;
import chess.domain.gamestate.State;
import chess.domain.team.Team;
import chess.utils.BoardUtil;
import java.sql.PreparedStatement;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcRoomDao implements RoomDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcRoomDao(final DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public long insert(Room room) {
        String sql = "INSERT INTO rooms (name, state, currentteam) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        this.jdbcTemplate.update((con) -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, room.getName());
            ps.setString(2, room.getState().getValue());
            ps.setString(3, room.getCurrentTeam().getValue());
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public void update(Room room) {
        String sql = "UPDATE rooms SET state = ?, currentteam = ? WHERE id = ?";
        this.jdbcTemplate.update(
                sql,
                room.getState().getValue(),
                room.getCurrentTeam().getValue(),
                room.getId()
        );
    }

    @Override
    public Room findById(long roomId) {
        String sql = "SELECT * FROM rooms WHERE id = ?";
        return this.jdbcTemplate.queryForObject(
                sql,
                (rs, rowNum) ->
                    new Room(
                            rs.getLong("id"),
                            rs.getString("name"),
                            State.generateState(rs.getString("state"), BoardUtil.generateInitialBoard()),
                            Team.of(rs.getString("currentteam"))
                    )
                ,
                roomId
        );
    }

    @Override
    public List<Room> findAll() {
        String sql = "SELECT * FROM rooms";
        return this.jdbcTemplate.query(
                sql,
                (rs, rowNum) ->
                        new Room(
                                rs.getLong("id"),
                                rs.getString("name"),
                                State.generateState(rs.getString("state"), BoardUtil.generateInitialBoard()),
                                Team.of(rs.getString("currentteam"))
                        )
        );
    }

    @Override
    public Room findByName(String roomName) {
        String sql = "SELECT * FROM rooms WHERE name = ?";
        return this.jdbcTemplate.queryForObject(
                sql,
                (rs, rowNum) ->
                        new Room(
                                rs.getLong("id"),
                                rs.getString("name"),
                                State.generateState(rs.getString("state"), BoardUtil.generateInitialBoard()),
                                Team.of(rs.getString("currentteam"))
                        )
                ,
                roomName
        );
    }

    @Override
    public boolean roomExists(String roomName) {
        String sql = "SELECT COUNT(*) FROM rooms WHERE name = ?";
        int count = this.jdbcTemplate.queryForObject(
                sql,
                Integer.class,
                roomName
        );
        return count > 0;
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM rooms";
        this.jdbcTemplate.update(sql);
    }
}
