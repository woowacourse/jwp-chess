package chess.domain.auth;

import static chess.util.HashUtil.hash;

import java.util.Map;
import org.springframework.boot.json.BasicJsonParser;

public class AuthCredentials {

    private static final int PASSWORD_MIN_LENGTH = 5;
    private static final BasicJsonParser jsonParser = new BasicJsonParser();

    private final String name;
    private final String password;

    public AuthCredentials(String name, String password) {
        validateLength(password);
        this.name = name;
        this.password = password;
    }

    public static AuthCredentials of(String json) {
        Map<String, Object> map = jsonParser.parseMap(json);
        return new AuthCredentials((String) map.get("name"), (String) map.get("password"));
    }

    public EncryptedAuthCredentials toEncrypted() {
        return new EncryptedAuthCredentials(name, hash(password));
    }

    private void validateLength(String password) {
        if (password.length() < PASSWORD_MIN_LENGTH) {
            throw new IllegalArgumentException("비밀번호는 최소 5글자 이상이어야 합니다!");
        }
    }

    @Override
    public String toString() {
        return "AuthCredentials{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
