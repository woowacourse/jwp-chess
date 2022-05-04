package chess.domain.room;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class Password {

    private final String hashPassword;

    private Password(final String hashPassword) {
        this.hashPassword = hashPassword;
    }

    public static Password fromPlain(final String plainPassword) {
        return new Password(toHash(plainPassword));
    }

    public static Password fromHash(final String hashPassword) {
        return new Password(hashPassword);
    }

    private static String toHash(final String plainPassword) {
        validate(plainPassword);
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    private static void validate(final String plainPassword) {
        if (plainPassword.isBlank()) {
            throw new IllegalArgumentException("비밀번호에 공백은 허용되지 않습니다.");
        }
    }

    public boolean isSame(final String plainPassword) {
        return BCrypt.checkpw(plainPassword, hashPassword);
    }

    public String getHashPassword() {
        return hashPassword;
    }
}
