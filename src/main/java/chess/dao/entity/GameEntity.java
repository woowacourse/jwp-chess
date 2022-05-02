package chess.dao.entity;

public class GameEntity {

    private Long id;
    private final String title;
    private final String password;
    private final String turn;
    private final String status;

    public GameEntity(Long id, String title, String password, String turn, String status) {
        this.id = id;
        this.title = title;
        this.password = password;
        this.turn = turn;
        this.status = status;
    }

    public GameEntity(String title, String password, String turn, String status) {
        this.title = title;
        this.password = password;
        this.turn = turn;
        this.status = status;
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

    public String getTurn() {
        return turn;
    }

    public String getStatus() {
        return status;
    }
}
