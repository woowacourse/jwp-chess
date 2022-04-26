package chess.entity;

public class ChessGameEntity {

    private Long id;
    private String title;
    private String password;

    public ChessGameEntity(Long id, String title, String password) {
        this.id = id;
        this.title = title;
        this.password = password;
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
