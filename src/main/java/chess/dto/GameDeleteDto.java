package chess.dto;

public class GameDeleteDto {

    private int id;
    private String password;

    public GameDeleteDto() {
    }

    public GameDeleteDto(int id, String password) {
        this.id = id;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }
}
