package chess.controller.dto;

public class RoomRequestDto {
    private String title;
    private boolean locked;
    private String password;

    public RoomRequestDto() {
    }

    public RoomRequestDto(String title, boolean locked, String password) {
        this.title = title;
        this.locked = locked;
        this.password = password;
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
