package chess.domain;

import java.util.Objects;

public class Room {

    private final long no;
    private final String title;
    private final String password;
    private boolean running;

    public Room(long no, String title, String password, boolean running) {
        this.no = no;
        this.title = title;
        this.password = password;
        this.running = running;
    }

    public Room(long no, String title) {
        this(no, title, "", true);
    }

    public static Room create(String title, String password) {
        validateTitle(title);
        validatePassword(password);
        return new Room(0, title, password, true);
    }

    private static void validateTitle(String title) {
        if (Objects.isNull(title) || title.isBlank()) {
            throw new IllegalArgumentException("제목은 필수 입력값입니다.");
        }
    }

    private static void validatePassword(String password) {
        if (Objects.isNull(password) || password.isBlank()) {
            throw new IllegalArgumentException("비밀번호는 필수 입력값입니다.");
        }
    }

    public boolean isPassword(String password) {
        return this.password.equals(password);
    }

    public void end() {
        running = false;
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

    public boolean isRunning() {
        return running;
    }
}
