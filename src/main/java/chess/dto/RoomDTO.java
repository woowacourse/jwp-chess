package chess.dto;

public final class RoomDTO {
    private final int id;
    private final String blackNickname;
    private final String whiteNickname;
    private final String title;
    private final String status;

    public RoomDTO(final int id, final String blackNickname, final String whiteNickname, final String title, final String status) {
        this.id = id;
        this.blackNickname = blackNickname;
        this.whiteNickname = whiteNickname;
        this.title = title;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getBlackNickname() {
        return blackNickname;
    }

    public String getWhiteNickname() {
        return whiteNickname;
    }

    public String getTitle() {
        return title;
    }

    public String getStatus() {
        return status;
    }
}
