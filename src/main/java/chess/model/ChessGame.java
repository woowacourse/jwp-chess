package chess.model;

public class ChessGame {

    private Long id;
    private String title;
    private String password;

    public ChessGame(Long id, String title, String password) {
        this.id = id;
        this.title = title;
        this.password = password;
    }

    public boolean isSamePassword(String password) {
        return this.password.equals(password);
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPassword() {
        return password;
    }
}
