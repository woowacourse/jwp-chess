package chess.controller.dto;

public class RoomRequestDto {
    private String title;
    private boolean locked;
    private String password;
    private String nickname;

    public RoomRequestDto(String title, boolean locked, String password, String nickname) {
        this.title = title;
        this.locked = locked;
        this.password = password;
        this.nickname = nickname;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public RoomRequestDto() {
    }

    public String getTitle() {
        return title;
    }

    public boolean isLocked() {
        return locked;
    }

    public String getPassword() {
        return password;
    }
}
