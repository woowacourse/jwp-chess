package chess.model.room;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class Password {

    private final String hashedPassword;

    private Password(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public static Password ofPlainText(String plainText) {
        return new Password(BCrypt.hashpw(plainText, BCrypt.gensalt()));
    }

    public static Password ofHashedText(String hashedText) {
        return new Password(hashedText);
    }

    public boolean isSamePassword(String plainText) {
        return BCrypt.checkpw(plainText, hashedPassword);
    }

    public String getHashedPassword() {
        return hashedPassword;
    }
}
