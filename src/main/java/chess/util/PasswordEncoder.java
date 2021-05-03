package chess.util;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordEncoder {

    private static final String ENCODING_ALGORITHM = "SHA-256";
    private static final int SIGNUM = 1;
    private static final String FORMAT = "%064x";

    private PasswordEncoder() {
    }

    public static String encodingPassword(final String rawPassword) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(ENCODING_ALGORITHM);
            messageDigest.update(rawPassword.getBytes(StandardCharsets.UTF_8));
            return String.format(FORMAT, new BigInteger(SIGNUM, messageDigest.digest()));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("암호화를 위한 인스턴스 탐색에 실패했습니다.");
        }
    }

}
