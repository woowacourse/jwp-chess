package chess.dto.room;

import javax.validation.constraints.NotNull;

public class RoomRegistrationDTO {

    @NotNull
    private final String name;
    @NotNull
    private final String password;

    public RoomRegistrationDTO(String name, String password) {
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
