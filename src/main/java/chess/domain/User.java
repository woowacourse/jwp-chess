package chess.domain;

import static chess.domain.UserStatus.IN_GAME;

public class User {
    private final String name;
    private final String pw;
    private final UserStatus status;

    public User(String name, String pw, UserStatus status) {
        this.name = name;
        this.pw = pw;
        this.status = status;
    }

    public boolean checkPassword(String pw) {
        return this.pw.equals(pw);
    }

    public String getName() {
        return name;
    }

    public boolean inGame() {
        return IN_GAME.equals(status);
    }
}
