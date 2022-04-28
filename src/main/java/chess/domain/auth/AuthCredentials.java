package chess.domain.auth;

import static chess.util.HashUtils.hash;

import java.util.Map;
import org.springframework.boot.json.BasicJsonParser;

public class AuthCredentials {

    private static final BasicJsonParser jsonParser = new BasicJsonParser();

    private final String name;
    private final String password;

    private AuthCredentials(String name, String password) {
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

    @Override
    public String toString() {
        return "AuthCredentials{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
