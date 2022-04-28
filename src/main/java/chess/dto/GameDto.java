package chess.dto;

public class GameDto {
    private int id;
    private String title;

    public GameDto(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
