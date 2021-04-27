package chess.domain;

public class User {
    private final String name;
    private final String pw;
    private final Long roomId;

    public User(String name, String pw, Long roomId) {
        this.name = name;
        this.pw = pw;
        this.roomId = roomId;
    }

    public boolean checkPassword(String pw) {
        return this.pw.equals(pw);
    }

    public String getName() {
        return name;
    }

    public Long getRoomId() {
        return roomId;
    }

    public boolean inGame() {
        System.out.println("inGame roomid : " + roomId);
        return roomId != 0;
    }
}
