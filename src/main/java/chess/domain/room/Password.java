package chess.domain.room;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class Password {

    private final String hashPassword;

    public Password(final String plainPassword) {
        this.hashPassword = toHash(plainPassword);
    }

    private String toHash(final String plainPassword) {
        validate(plainPassword);
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    private void validate(final String plainPassword) {
        if (plainPassword.isBlank()) {
            throw new IllegalArgumentException("비밀번호에 공백은 허용되지 않습니다.");
        }
    }

    public boolean isSame(final String plainPassword) {
        return BCrypt.checkpw(plainPassword, hashPassword);
    }
}
