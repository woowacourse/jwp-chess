package chess.model.room;

public class Room {

    private final int id;
    private final String title;
    private final String password;
    private final int boardId;

    public Room(String title, String password, int boardId) {
        this(0, title, password, boardId);
    }

    public Room(int id, String title, String password, int boardId) {
        this.id = id;
        this.title = title;
        this.password = password;
        this.boardId = boardId;
    }

    public boolean isSamePassword(String password) {
        return this.password.equals(password);
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPassword() {
        return password;
    }

    public int getBoardId() {
        return boardId;
    }
}
