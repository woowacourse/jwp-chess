package chess.dto.room;

import lombok.Getter;

@Getter
public class RoomCreateDTO {
    private final String name;
    private final String nickname;
    private final String password;

    public RoomCreateDTO(String name, String nickname, String password) {
        this.name = name;
        this.nickname = nickname;
        this.password = password;
    }
}
