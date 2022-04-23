package chess.domain.auth;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class AuthCredentials {

    private static final String HASH_ALGORITHM = "PBKDF2WithHmacSHA1";
    private static final byte[] SECRET_SALT = "should_be_hidden_before_deployment".getBytes();
    private static final int HASH_ITERATION_COUNT = 65536;
    private static final int KEY_BIT_LENGTH = 128;
    private static final SecretKeyFactory hashFactory;

    static {
        try {
            hashFactory = SecretKeyFactory.getInstance(HASH_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException("클래스 생성 과정에서 문제가 발생하였습니다.");
        }
    }

    private final String name;
    private final String password;

    public AuthCredentials(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public EncryptedAuthCredentials toEncrypted() {
        return EncryptedAuthCredentials.of(name, hashPassword());
    }

    private byte[] hashPassword() {
        try {
            KeySpec keySpec = new PBEKeySpec(password.toCharArray(),
                    SECRET_SALT, HASH_ITERATION_COUNT, KEY_BIT_LENGTH);
            return hashFactory.generateSecret(keySpec).getEncoded();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            throw new RuntimeException("비밀번호 암호화 과정에서 문제가 발생했습니다.");
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
