package chess.domain.auth;

public class EncryptedAuthCredentials {

    private final String name;
    private final String password;

    public EncryptedAuthCredentials(String name, String password) {
        this.name = name;
        this.password = password;
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
