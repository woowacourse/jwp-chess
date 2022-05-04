package chess.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RoomRequest {

    @JsonProperty("room_name")
    @JsonAlias("roomName")
    private final String roomName;
    private final String password;

    private RoomRequest() {
        this(null, null);
    }

    public RoomRequest(String roomName, String password) {
        this.roomName = roomName;
        this.password = password;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getPassword() {
        return password;
    }
}
