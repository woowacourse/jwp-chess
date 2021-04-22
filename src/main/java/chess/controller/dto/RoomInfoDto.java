package chess.controller.dto;

public class RoomInfoDto {
    private final Long id;
    private final String name;

    public RoomInfoDto(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

