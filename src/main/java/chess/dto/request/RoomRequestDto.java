package chess.dto.request;

public class RoomRequestDto {
    private String name;

    private RoomRequestDto() {
    }

    public RoomRequestDto(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
