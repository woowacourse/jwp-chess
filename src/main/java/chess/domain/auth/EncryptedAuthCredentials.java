package chess.domain.auth;

import java.util.Base64;

public class EncryptedAuthCredentials {

    private final String name;
    private final String password;

    public EncryptedAuthCredentials(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public static EncryptedAuthCredentials of(String name, byte[] hashedPassword) {
        return new EncryptedAuthCredentials(name,
                Base64.getEncoder().encodeToString(hashedPassword));
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "EncryptedAuthInfo{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
