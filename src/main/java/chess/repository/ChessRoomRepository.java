package chess.repository;

import chess.model.room.Room;
import chess.model.status.Status;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ChessRoomRepository implements RoomRepository<Room> {

    private final JdbcTemplate jdbcTemplate;

    public ChessRoomRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public List<Room> findAllByBoardStatus(Status status) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(
                "SELECT * FROM room r " +
                        "JOIN board b on r.board_id=b.id WHERE b.status=?", status.name());
        List<Room> rooms = new ArrayList<>();
        while (sqlRowSet.next())
            rooms.add(new Room(
                    sqlRowSet.getInt("id"),
                    sqlRowSet.getString("title"),
                    sqlRowSet.getInt("board_id"))
            );
        return rooms;
    }

    @Override
    public Room save(Room room) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        simpleJdbcInsert.withTableName("room").usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("title", room.getTitle());
        parameters.put("board_id", room.getBoardId());

        Number number = simpleJdbcInsert.executeAndReturnKey(parameters);
        return new Room(number.intValue(), room.getTitle(), room.getBoardId());
    }

    @Override
    public Room getById(int roomId) {
        return jdbcTemplate.queryForObject("SELECT * FROM room WHERE id=?", getRoomRowMapper(), roomId);
    }

    private RowMapper<Room> getRoomRowMapper() {
        return (resultSet, rowNum) -> new Room(
                resultSet.getInt("id"),
                resultSet.getString("title"),
                resultSet.getInt("board_id")
        );
    }
}
