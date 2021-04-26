package chess.dao.dto.game;

public class GameDto {

    private Long id;
    private String roomName;
    private String whiteUsername;
    private String blackUsername;

    public GameDto() {
    }

    public GameDto(Long id, String roomName, String whiteUsername, String blackUsername) {
        this.id = id;
        this.roomName = roomName;
        this.whiteUsername = whiteUsername;
        this.blackUsername = blackUsername;
    }

    public GameDto(String roomName, String whiteUsername, String blackUsername) {
        this.roomName = roomName;
        this.whiteUsername = whiteUsername;
        this.blackUsername = blackUsername;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getWhiteUsername() {
        return whiteUsername;
    }

    public void setWhiteUsername(String whiteUsername) {
        this.whiteUsername = whiteUsername;
    }

    public String getBlackUsername() {
        return blackUsername;
    }

    public void setBlackUsername(String blackUsername) {
        this.blackUsername = blackUsername;
    }
}
