package chess.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CryptoUtils {

    private CryptoUtils() {
    }

    public static String bytesToHexString(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }

    public static String encrypt(String text) {
        MessageDigest messageDigest = getMessageDigestInstance();
        byte[] hash = messageDigest.digest(text.getBytes(StandardCharsets.UTF_8));
        String encryptedText = bytesToHexString(hash);

        return encryptedText;
    }

    private static MessageDigest getMessageDigestInstance() {
        try {
            return MessageDigest.getInstance("SHA3-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
