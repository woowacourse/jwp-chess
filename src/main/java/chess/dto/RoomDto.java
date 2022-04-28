package chess.dto;

public class RoomDto {
    private final Long id;
    private final String turn;
    private final String name;

    public RoomDto(Long id, String turn, String name) {
        this.id = id;
        this.turn = turn;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getTurn() {
        return turn;
    }

    public String getName() {
        return name;
    }
}
