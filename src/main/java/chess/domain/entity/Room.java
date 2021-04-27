package chess.domain.entity;

public class Room {
    private int id;
    private String title;
    private int blackUserId;
    private int whiteUserId;
    private int status;

    public Room(int id, String title, int blackUserId, int whiteUserId, int status) {
        this.id = id;
        this.title = title;
        this.blackUserId = blackUserId;
        this.whiteUserId = whiteUserId;
        this.status = status;
    }

    public Room(int black_user, int white_user) {
        this(0, null, black_user, white_user, -1);
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getBlackUserId() {
        return blackUserId;
    }

    public int getWhiteUserId() {
        return whiteUserId;
    }

    public int getStatus() {
        return status;
    }
}
