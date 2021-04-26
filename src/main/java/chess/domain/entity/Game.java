package chess.domain.entity;

import java.util.Objects;

public class Game implements Entity<Long> {

    private Long id;
    private String roomName;
    private String whiteUsername;
    private String blackUsername;

    public Game() {
    }

    public Game(Long id, String roomName, String whiteUsername, String blackUsername) {
        this.id = id;
        this.roomName = roomName;
        this.whiteUsername = whiteUsername;
        this.blackUsername = blackUsername;
        validateGame();
    }

    public Game(String roomName, String whiteUsername, String blackUsername) {
        this(null, roomName, whiteUsername, blackUsername);
    }

    private void validateGame() {
        validateNull();
        validateEmpty();
    }

    private void validateNull() {
        Objects.requireNonNull(this.roomName, "방이름은 null 일 수 없습니다.");
        Objects.requireNonNull(this.whiteUsername, "유저 이름은 null 일 수 없습니다.");
        Objects.requireNonNull(this.blackUsername, "유저 이름은 null 일 수 없습니다.");
    }

    private void validateEmpty() {
        if (this.roomName.isEmpty()) {
            throw new IllegalArgumentException("방 이름은 1글자 이상 작성해야합니다.");
        }
        if (this.whiteUsername.isEmpty() || this.blackUsername.isEmpty()) {
            throw new IllegalArgumentException("유저 이름은 1글자 이상 작성해야합니다.");
        }
    }

    @Override
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
