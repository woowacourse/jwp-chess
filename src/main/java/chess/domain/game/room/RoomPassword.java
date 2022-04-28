package chess.domain.game.room;

import chess.util.Sha256;

import java.security.NoSuchAlgorithmException;

public class RoomPassword {

    private final String value;

    private RoomPassword(String value) {
        this.value = value;
    }

    public static RoomPassword createByPlainText(String roomPassword) {
        validateNotEmpty(roomPassword);

        try {
            String hashedPassword = Sha256.encrypt(roomPassword);
            return new RoomPassword(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace(); // TODO: 적절한 처리 필요
            return null;
        }
    }

    private static void validateNotEmpty(String roomPassword) {
        if (roomPassword.isEmpty()) {
            throw new IllegalArgumentException("빈 패스워드로 설정할 수 없습니다.");
        }
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
