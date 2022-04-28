package chess.dto;

public class ChessGameNameDto {

    private final int id;
    private final String name;

    public ChessGameNameDto(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
