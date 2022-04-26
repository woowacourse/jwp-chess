package chess.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RoomNameDto {

    private final String name;

    public RoomNameDto() {
        this.name = "";
    }

    public RoomNameDto(final String name) {
        this.name = name;
    }

    public static RoomNameDto from(final ResultSet resultSet) throws SQLException {
        return new RoomNameDto(resultSet.getString("name"));
    }

    public String getName() {
        return name;
    }
}
