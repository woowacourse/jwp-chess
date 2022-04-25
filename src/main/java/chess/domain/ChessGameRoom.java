package chess.domain;

import java.util.Objects;

public class ChessGameRoom {

    private final long id;
    private final String title;
    private final String password;

    public ChessGameRoom(long id, String title, String password) {
        Objects.requireNonNull(title, "title은 null이 들어올 수 없습니다.");
        Objects.requireNonNull(password, "password는 null이 들어올 수 없습니다.");
        this.id = id;
        this.title = title;
        this.password = password;
    }

    public void checkPassword(String password) {
        Objects.requireNonNull(password, "password는 null이 들어올 수 없습니다.");
        if (!this.password.equals(password)) {
            throw new IllegalStateException("비밀번호가 일치하지 않습니다.");
        }
    }
}
