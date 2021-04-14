package chess.dto;


public class CreateRoomRequestDTO {
    private String name;

    public CreateRoomRequestDTO() {
    }

    public CreateRoomRequestDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
