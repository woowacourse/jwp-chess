package wooteco.chess.dto;

import javax.validation.constraints.NotEmpty;

public class RoomResponseDto {

    private Long id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String password;

    private RoomResponseDto() {
    }

    public RoomResponseDto(final Long id, @NotEmpty final String name, @NotEmpty final String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
