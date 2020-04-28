package wooteco.chess.dto;

import javax.validation.constraints.NotEmpty;
import java.util.UUID;

public class RoomDto {

    private UUID id;

    @NotEmpty
    private String roomName;

    @NotEmpty
    private String password;

    public RoomDto(@NotEmpty final UUID id, @NotEmpty final String roomName, @NotEmpty final String password) {
        this.id = id;
        this.roomName = roomName;
        this.password = password;
    }

    public UUID getId() {
        return id;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getPassword() {
        return password;
    }
}
