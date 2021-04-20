package chess.dto.room;

public final class RoomNameDTO {
    private String name;

    public RoomNameDTO() {
    }

    public RoomNameDTO(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isEmpty() {
        return name.isEmpty();
    }
}
