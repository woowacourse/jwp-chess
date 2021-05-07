package chess.domain.user;

import chess.domain.Entity;

import java.util.Objects;

public class User extends Entity<Long> {

    private final String username;
    private final String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    private void validateUser(String username, String password) {
        validateNull(username, password);
    }

    private void validateNull(String username, String password) {
        Objects.requireNonNull(username, "유저 이름은 null일 수 없습니다.");
        Objects.requireNonNull(password, "유저 비밀번호는 null일 수 없습니다.");
    }

    private void validateEmpty(String username, String password) {
        if (username.isEmpty()) {
            throw new IllegalArgumentException("유저 이름은 공백일 수 없습니다.");
        }
        if (password.isEmpty()) {
            throw new IllegalArgumentException("유저 비밀번호는 공백일 수 없습니다.");
        }
    }

    public boolean checkPassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }
}
