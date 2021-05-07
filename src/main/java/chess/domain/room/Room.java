package chess.domain.room;

import chess.domain.Entity;

import java.util.Objects;

public class Room extends Entity<Long> {

    private final String roomName;
    private final Long whiteUserId;
    private Long blackUserId;

    public Room(String roomName, Long whiteUserId) {
        this(roomName, whiteUserId, null);
    }

    public Room(String roomName, Long whiteUserId, Long blackUserId) {
        this.roomName = roomName;
        this.whiteUserId = whiteUserId;
        this.blackUserId = blackUserId;
    }

    public static Room of(final String roomName, final Long whiteUserId) {
        validateRoom(roomName, whiteUserId);
        return new Room(roomName, whiteUserId);
    }

    public static Room of(final String roomName, final Long whiteUserId, final Long blackUserId) {
        return new Room(roomName, whiteUserId, blackUserId);
    }

    private static void validateRoom(final String roomName, final Long whiteUserId) {
        validateNull(roomName, whiteUserId);
        validateEmpty(roomName, whiteUserId);
    }

    private static void validateNull(final String roomName, final Long whiteUserId) {
        Objects.requireNonNull(roomName, "방 생성시 방 이름은 null 일 수 없습니다.");
        Objects.requireNonNull(whiteUserId, "방 생성시 White User 는 null이 될 수 없습니다.");
    }

    private static void validateEmpty(final String roomName, final Long whiteUserId) {
        if (roomName.isEmpty()) {
            throw new IllegalArgumentException("방 이름은 1글자 이상 작성해야합니다.");
        }
    }

    public void joinBlackUser(Long blackUserId) {
        validateBlackUserId(blackUserId);
        this.blackUserId = blackUserId;
    }

    private void validateBlackUserId(Long blackUserId) {
        Objects.requireNonNull(blackUserId, "Black User 입장시 ID는 null일 수 없습니다.");
    }

    public String roomName() {
        return this.roomName;
    }

    public Long whiteUserId() {
        return this.whiteUserId;
    }

    public Long blackUserId() {
        return this.blackUserId;
    }
}
