package chess.domain;

public class Room {

    private final long no;
    private final String title;
    private final String password;

    public Room(long no, String title, String password) {
        this.no = no;
        this.title = title;
        this.password = password;
    }

    public Room(long no, String title) {
        this(no, title, "");
    }

    public static Room create(String title, String password) {
        return new Room(0, title, password);
    }

    public long getNo() {
        return no;
    }

    public String getTitle() {
        return title;
    }

    public String getPassword() {
        return password;
    }
}
