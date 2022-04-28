package chess.web.dto.game;

public class TitleDto {

    private final int id;
    private final String title;

    public TitleDto(int id, String title) {
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
