package chess.domain;

public final class Room {

    private final String title;
    private final String password;

    public Room(String title, String password) {
        this.title = title;
        this.password = password;
    }

    public void checkPassword(String otherPassword) {
        if (!password.equals(otherPassword)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }
}
