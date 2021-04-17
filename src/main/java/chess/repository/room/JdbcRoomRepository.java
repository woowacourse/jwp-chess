package chess.repository.room;

import chess.domain.game.Room;
import chess.domain.gamestate.State;
import chess.domain.team.Team;
import chess.utils.BoardUtil;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcRoomRepository implements RoomRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcRoomRepository(final DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public long insert(Room room) {
        String sql = "INSERT INTO rooms (name, state, currentteam) VALUES (?, ?, ?)";
        return this.jdbcTemplate.update(
                sql,
                room.getName(),
                room.getState().getValue(),
                room.getCurrentTeam().getValue()
        );
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
    public Room findRoomById(long roomId) {
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
    public Room findRoomByRoomName(String roomName) {
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
    public boolean isExistRoomName(String roomName) {
        String sql = "SELECT COUNT(*) FROM rooms WHERE name = ?";
        int count = this.jdbcTemplate.queryForObject(
                sql,
                Integer.class,
                roomName
        );

        if (count == 0) {
            return true;
        }
        return false;
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM rooms";
        this.jdbcTemplate.update(sql);
    }

    @Override
    public int count() {
        String sql = "SELECT COUNT(*) FROM rooms";
        return this.jdbcTemplate.queryForObject(sql, Integer.class);
    }
}
