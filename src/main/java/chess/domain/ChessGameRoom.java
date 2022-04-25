package chess.domain;

import java.util.Objects;

public class ChessGameRoom {

    private final Long id;
    private final String title;
    private final String password;

    public ChessGameRoom(Long id, String title, String password) {
        Objects.requireNonNull(title, "title은 null이 들어올 수 없습니다.");
        Objects.requireNonNull(password, "password는 null이 들어올 수 없습니다.");
        this.id = id;
        this.title = title;
        this.password = password;
    }

    public ChessGameRoom(final String title, final String password) {
        this(null, title, password);
    }

    public void checkPassword(String password) {
        Objects.requireNonNull(password, "password는 null이 들어올 수 없습니다.");
        if (!this.password.equals(password)) {
            throw new IllegalStateException("비밀번호가 일치하지 않습니다.");
        }
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPassword() {
        return password;
    }
}
