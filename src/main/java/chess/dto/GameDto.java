package chess.dto;

public class GameDto {

    private int id;
    private String roomName;
    private String whiteName;
    private String blackName;
    private boolean isFinished;
    private String password;

    public GameDto() {
    }

    public GameDto(int id, String roomName, String whiteName, String blackName, boolean isFinished) {
        this.id = id;
        this.roomName = roomName;
        this.whiteName = whiteName;
        this.blackName = blackName;
        this.isFinished = isFinished;
    }

    public int getId() {
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

    public String getPassword() {
        return password;
    }
}
