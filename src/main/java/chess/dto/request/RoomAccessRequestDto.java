package chess.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RoomAccessRequestDto {

    private final String password;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public RoomAccessRequestDto(@JsonProperty("password") String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
