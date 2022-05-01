package chess.domain.room;

public class RoomPassword {

    private final String password;

    public RoomPassword(String password) {
        this.password = password;
    }

    public static RoomPassword of(String password) {
        return new RoomPassword(password);
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return password;
    }
}
