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

    public RoomRequestDto() {
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
