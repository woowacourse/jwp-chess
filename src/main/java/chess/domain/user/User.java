package chess.domain.user;

import chess.domain.Entity;
import chess.exception.PasswordMissMatchException;

import java.util.Objects;

public class User extends Entity<Long> {

    private final String name;
    private final String password;

    public User(final String name, final String password) {
        this(null, name, password);
    }

    public User(final Long id, final String name, final String password) {
        super(id);
        validateUser(name, password);
        this.name = name;
        this.password = password;
    }

    private void validateUser(String name, String password) {
        validateNull(name, password);
        validateEmpty(name, password);
    }

    private void validateNull(String name, String password) {
        Objects.requireNonNull(name, "유저 이름은 null일 수 없습니다.");
        Objects.requireNonNull(password, "유저 비밀번호는 null일 수 없습니다.");
    }

    private void validateEmpty(String name, String password) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("유저 이름은 공백일 수 없습니다.");
        }
        if (password.isEmpty()) {
            throw new IllegalArgumentException("유저 비밀번호는 공백일 수 없습니다.");
        }
    }

    public boolean isSameName(final User user) {
        return this.name.equals(user.name);
    }

    public void checkPassword(final String inputPassword) {
        if (isDifferentPassword(inputPassword)) {
            throw new PasswordMissMatchException("비밀번호가 일치하지 않습니다!");
        }
    }

    private boolean isDifferentPassword(final String inputPassword) {
        return !this.password.equals(inputPassword);
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
