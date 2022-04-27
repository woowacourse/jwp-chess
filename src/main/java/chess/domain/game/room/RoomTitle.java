package chess.domain.game.room;

public class RoomTitle {
    private static final int TITLE_LENGTH_LIMIT = 50;

    private final String value;

    private RoomTitle(String value) {
        this.value = value;
        validate(value);
    }

    public static RoomTitle from(String value) {
        return new RoomTitle(value);
    }

    private void validate(String value) {
        validateNotEmpty(value);
        validateLengthLimit(value);
    }

    private void validateLengthLimit(String value) {
        if (value.length() >= TITLE_LENGTH_LIMIT) {
            throw new IllegalArgumentException("빈 제목으로 생성할 수 없습니다.");
        }
    }

    private void validateNotEmpty(String value) {
        if (value.isEmpty()) {
            throw new IllegalArgumentException("빈 제목으로 생성할 수 없습니다.");
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

        RoomTitle roomTitle = (RoomTitle) o;

        return value.equals(roomTitle.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "RoomTitle{" +
                "value='" + value + '\'' +
                '}';
    }
}
