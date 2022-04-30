package chess.util;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class HashUtils {

    private static final String HASH_ALGORITHM = "PBKDF2WithHmacSHA1";
    private static final byte[] SECRET_SALT = "should_be_hidden_before_deployment".getBytes();
    private static final int HASH_ITERATION_COUNT = 65536;
    private static final int KEY_BIT_LENGTH = 128;

    private static final SecretKeyFactory hashFactory;

    // TODO: 수정
    static {
        try {
            hashFactory = SecretKeyFactory.getInstance(HASH_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException("클래스 생성 과정에서 문제가 발생하였습니다.");
        }
    }

    private HashUtils() {
    }

    public static String hash(String value) {
        try {
            KeySpec keySpec = toKeySpec(value);
            byte[] hashedPassword = hashFactory.generateSecret(keySpec).getEncoded();
            return toString(hashedPassword);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            throw new RuntimeException("암호화 과정에서 문제가 발생했습니다.");
        }
    }

    private static PBEKeySpec toKeySpec(String value) {
        char[] valueChars = value.toCharArray();
        return new PBEKeySpec(valueChars, SECRET_SALT, HASH_ITERATION_COUNT, KEY_BIT_LENGTH);
    }

    private static String toString(byte[] hashedPassword) {
        return Base64.getEncoder().encodeToString(hashedPassword);
    }
}
