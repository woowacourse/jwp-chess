package chess.dto;

public class BoardInfoDto {
    private final int id;
    private final String name;

    public BoardInfoDto(int id, String name) {
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
