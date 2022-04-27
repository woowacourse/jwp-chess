package chess.board;

public class Room {

    private final int boardId;
    private final String title;
    private final String password;

    public Room(int boardId, String title, String password) {
        this.boardId = boardId;
        this.title = title;
        this.password = password;
    }

    public int getBoardId() {
        return boardId;
    }

    public String getTitle() {
        return title;
    }

    public String getPassword() {
        return password;
    }
}
