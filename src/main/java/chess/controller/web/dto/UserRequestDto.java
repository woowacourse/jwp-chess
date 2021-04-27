package chess.controller.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserRequestDto {
    @JsonProperty
    private final long roomId;
    @JsonProperty
    private final String password;

    public UserRequestDto(long roomId, String password) {
        this.roomId = roomId;
        this.password = password;
    }

    public long getRoomId() {
        return roomId;
    }

    public String getPassword() {
        return password;
    }
}
