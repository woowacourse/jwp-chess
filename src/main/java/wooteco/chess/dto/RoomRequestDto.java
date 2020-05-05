package wooteco.chess.dto;

import javax.validation.constraints.NotEmpty;

public class RoomRequestDto {
    @NotEmpty
    private final String name;
    @NotEmpty
    private final String password;

    public RoomRequestDto(@NotEmpty final String name, @NotEmpty final String password) {
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
