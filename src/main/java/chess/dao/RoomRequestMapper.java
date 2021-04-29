package chess.dao;

import chess.domain.Room;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class RoomRequestMapper implements RowMapper<Room> {

    private static final String ROOM_ID = "id";
    private static final String ROOM_NAME = "name";

    @Override
    public Room mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        int id = resultSet.getInt(ROOM_ID);
        String name = resultSet.getString(ROOM_NAME);
        return new Room(id, name);
    }

}
