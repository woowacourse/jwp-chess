package chess.domain.game.room;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RoomPassword {
    private static final Charset CHAR_SET = StandardCharsets.UTF_8;
    private static final String HASH_ALGORITHM = "SHA-256";

    private final String value;

    private RoomPassword(String value) {
        this.value = value;
    }

    public static RoomPassword from(String roomPassword) {
        try {
            String hashedPassword = hash(roomPassword);
            return new RoomPassword(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace(); // TODO: 적절한 처리 필요
            return null;
        }
    }

    private static String hash(String roomPassword) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance(HASH_ALGORITHM);
        messageDigest.update(roomPassword.getBytes(CHAR_SET));
        return new String(messageDigest.digest(), CHAR_SET);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RoomPassword that = (RoomPassword) o;

        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "RoomPassword{" +
                "roomPassword='" + value + '\'' +
                '}';
    }
}
