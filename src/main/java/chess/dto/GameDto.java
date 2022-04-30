package chess.dto;

public class GameDto {

    private int id;
    private String title;
    private String state;

    public GameDto(int id, String title, String state) {
        this.id = id;
        this.title = title;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getState() {
        return state;
    }
}
