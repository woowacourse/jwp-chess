package chess.dto;

public class RoomDto {
    private final long id;
    private final String turn;
    private final String name;

    public RoomDto(long id, String turn, String name) {
        this.id = id;
        this.turn = turn;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getTurn() {
        return turn;
    }

    public String getName() {
        return name;
    }
}
