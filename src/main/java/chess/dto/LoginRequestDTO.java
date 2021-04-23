package chess.dto;

import javax.validation.constraints.NotNull;

public class LoginRequestDTO {

    @NotNull
    private final int roomId;
    @NotNull
    private final String password;

    public LoginRequestDTO(int roomId, String password) {
        this.roomId = roomId;
        this.password = password;
    }

    public int getRoomId() {
        return roomId;
    }

    public String getPassword() {
        return password;
    }
}
