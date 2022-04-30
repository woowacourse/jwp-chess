package chess.dto;

public class GameDto {

    private int id;
    private String title;
    private String state;
    private String password;

    public GameDto(int id, String title, String state) {
        this.id = id;
        this.title = title;
        this.state = state;
    }

    public GameDto(int id, String title, String state, String password) {
        this.id = id;
        this.title = title;
        this.state = state;
        this.password = password;
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

    public String getPassword() {
        return password;
    }
}
