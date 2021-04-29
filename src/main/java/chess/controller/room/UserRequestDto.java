package chess.controller.room;

import com.fasterxml.jackson.annotation.JsonProperty;

class UserRequestDto {
    @JsonProperty
    private long roomId;
    @JsonProperty
    private String password;

    public UserRequestDto() {
    }

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
