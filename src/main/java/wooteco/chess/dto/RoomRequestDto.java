package wooteco.chess.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class RoomRequestDto {
    @NotEmpty
    private String name;
    @NotEmpty
    private String password;

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
