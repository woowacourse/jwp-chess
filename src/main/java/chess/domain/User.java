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
        if (this.pw.equals(pw)) {
            return true;
        }

        return false;
    }

    public String getName() {
        return name;
    }

    public boolean inGame() {
        if (IN_GAME.equals(status)) {
            return true;
        }

        return false;
    }
}
