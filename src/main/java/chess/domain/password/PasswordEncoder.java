package chess.domain.password;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordEncoder {

    public static String encrypt(String rawPassword) {
        MessageDigest messageDigest = getMessageDigest(rawPassword);
        return getEncryptedPassword(messageDigest);
    }

    private static MessageDigest getMessageDigest(String rawPassword) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(rawPassword.getBytes(StandardCharsets.UTF_8));
            return messageDigest;
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("비밀번호 암호화를 위한 인스턴스를 찾을 수 없습니다.");
        }
    }

    private static String getEncryptedPassword(MessageDigest messageDigest) {
        byte[] byteData = messageDigest.digest();
        StringBuilder sb = new StringBuilder();
        for (byte byteDatum : byteData) {
            sb.append(Integer.toString((byteDatum & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}
