package chess.dto;


public final class CreateRoomRequestDTO {
    private String name;

    public CreateRoomRequestDTO() {
    }

    public CreateRoomRequestDTO(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
