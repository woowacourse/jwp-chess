package chess.web.dto.game;

public class GameResponseDto {

    private final int id;
    private final String title;

    public GameResponseDto(int id, String title) {
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
