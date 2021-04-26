package chess.domain.room;

public class RoomInformation {
    private String title;
    private boolean locked;
    private String password;

    public RoomInformation(String title, boolean locked, String password) {
        this.title = title;
        this.locked = locked;
        this.password = password;
    }

    public String title() {
        return title;
    }

    public boolean isLocked() {
        return locked;
    }

    public boolean passwordIncorrect(String password) {
        return this.password.equals(password);
    }
}
