package chess.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RoomRequestDto {
    private final String name;
    private final String password;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public RoomRequestDto(@JsonProperty("name") final String name, @JsonProperty("password") final String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
