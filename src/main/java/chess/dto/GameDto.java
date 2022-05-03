package chess.dto;

public class GameDto {

    private long id;
    private String roomName;
    private String whiteName;
    private String blackName;
    private boolean isFinished;

    public GameDto() {
    }

    public GameDto(long id, String roomName, String whiteName, String blackName, boolean isFinished) {
        this.id = id;
        this.roomName = roomName;
        this.whiteName = whiteName;
        this.blackName = blackName;
        this.isFinished = isFinished;
    }

    public long getId() {
        return id;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getWhiteName() {
        return whiteName;
    }

    public String getBlackName() {
        return blackName;
    }

    public boolean isFinished() {
        return isFinished;
    }
}
