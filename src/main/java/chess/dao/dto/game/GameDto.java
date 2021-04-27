package chess.dao.dto.game;

public class GameDto {

    private Long id;
    private String roomName;
    private String whiteUsername;
    private String whitePassword;
    private String blackUsername;
    private String blackPassword;

    public GameDto() {
    }

    public GameDto(Long id, String roomName, String whiteUsername, String whitePassword,
                   String blackUsername, String blackPassword) {
        this.id = id;
        this.roomName = roomName;
        this.whiteUsername = whiteUsername;
        this.whitePassword = whitePassword;
        this.blackUsername = blackUsername;
        this.blackPassword = blackPassword;
    }

    public GameDto(String roomName, String whiteUsername, String whitePassword) {
        this.roomName = roomName;
        this.whiteUsername = whiteUsername;
        this.whitePassword = whitePassword;
    }

    public GameDto(String blackUsername, String blackPassword) {
        this.blackUsername = blackUsername;
        this.blackPassword = blackPassword;
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

    public String getWhitePassword() {
        return whitePassword;
    }

    public void setWhitePassword(String whitePassword) {
        this.whitePassword = whitePassword;
    }

    public String getBlackUsername() {
        return blackUsername;
    }

    public void setBlackUsername(String blackUsername) {
        this.blackUsername = blackUsername;
    }

    public String getBlackPassword() {
        return blackPassword;
    }

    public void setBlackPassword(String blackPassword) {
        this.blackPassword = blackPassword;
    }
}
