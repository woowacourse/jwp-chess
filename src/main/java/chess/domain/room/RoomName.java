package chess.domain.room;

public class RoomName {

    private static final int NAME_MAX_LENGTH = 10;

    private final String value;

    public RoomName(final String value) {
        validate(value);
        this.value = value;
    }

    private void validate(final String value) {
        if (value.isBlank()) {
            throw new IllegalArgumentException("방 이름에 공백은 허용되지 않습니다.");
        }
        if (value.length() > NAME_MAX_LENGTH) {
            throw new IllegalArgumentException("방 이름은 최대 " + NAME_MAX_LENGTH + "자 까지 허용됩니다.");
        }
    }

    public String getValue() {
        return value;
    }
}
