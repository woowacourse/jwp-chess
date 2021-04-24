package chess.dto.room;

import lombok.Getter;

@Getter
public class RoomCreateDTO {
    private final String name;
    private final String playerId;
    private final String password;

    public RoomCreateDTO(String name, String playerId, String password) {
        this.name = name;
        this.playerId = playerId;
        this.password = password;
    }
}
