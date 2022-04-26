package chess.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;

public class PasswordEncryptor {

    public static String encrypt(String password, String salt) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("서버의 비밀번호 암호화 과정에 문제가 있습니다.");
        }
        return String.format("%064x", new BigInteger(1, md.digest((password + salt).getBytes())));
    }

    public static String generateSalt() {
        byte[] bytes = new byte[16];
        new Random().nextBytes(bytes);
        return Arrays.toString(Base64.getEncoder().encode(bytes));
    }
}
