package chess.dto.request;

public class RoomRequestDto {
    private String name;

    public RoomRequestDto() {
    }

    public RoomRequestDto(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
