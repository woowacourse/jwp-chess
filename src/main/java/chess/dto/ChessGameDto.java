package chess.dto;

public class ChessGameDto {

    private String title;
    private String password;

    public ChessGameDto(String title, String password) {
        this.title = title;
        this.password = password;
    }

    public String getTitle() {
        return title;
    }

    public String getPassword() {
        return password;
    }
}
