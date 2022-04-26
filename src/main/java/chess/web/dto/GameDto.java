package chess.web.dto;

public class GameDto {

    private final int id;
    private final String title;

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
