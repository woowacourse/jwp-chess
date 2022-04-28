package chess.board;

public class Room {

    private final Long id;
    private final String turn;
    private final String title;
    private final String password;

    public Room(Long id, String turn, String title, String password) {
        this.id = id;
        this.turn = turn;
        this.title = title;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getTurn() {
        return turn;
    }

    public String getTitle() {
        return title;
    }

    public String getPassword() {
        return password;
    }
}
