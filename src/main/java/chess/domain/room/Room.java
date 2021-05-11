package chess.domain.room;

import chess.domain.Entity;

import java.util.Objects;

public class Room extends Entity<Long> {
    private static final Long NOT_EXIST_VALUE = 0L;

    private final Long gameId;
    private final String name;
    private final Long whiteUserId;
    private final Long blackUserId;

    public Room(final Long gameId, final String name, final Long whiteUserId) {
        this(null, gameId, name, whiteUserId, null);
    }

    public Room(final Long id, final Long gameId, final String name, final Long whiteUserId) {
        this(id, gameId, name, whiteUserId, null);
    }

    public Room(final Long gameId, final String name, final Long whiteUserId, final Long blackUserId) {
        this(null, gameId, name, whiteUserId, blackUserId);
    }

    public Room(final Long id, final Long gameId, final String name, final Long whiteUserId, final Long blackUserId) {
        super(id);
        validateRoom(name, whiteUserId);
        this.gameId = gameId;
        this.name = name;
        this.whiteUserId = whiteUserId;
        this.blackUserId = blackUserId;
    }

    private static void validateRoom(final String name, final Long whiteUserId) {
        validateNull(name, whiteUserId);
        validateEmpty(name);
    }

    private static void validateNull(final String name, final Long whiteUserId) {
        Objects.requireNonNull(name, "방 생성시 방 이름은 null 일 수 없습니다.");
        Objects.requireNonNull(whiteUserId, "방 생성시 White User 는 null이 될 수 없습니다.");
    }

    private static void validateEmpty(final String name) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("방 이름은 1글자 이상 작성해야합니다.");
        }
    }

    public Room joinBlackUser(final Long blackUserId) {
        validateBlackUserId(blackUserId);
        return new Room(this.id, this.gameId, this.name, this.whiteUserId, blackUserId);
    }

    private void validateBlackUserId(final Long blackUserId) {
        Objects.requireNonNull(blackUserId, "Black User ID는 null일 수 없습니다.");
    }

    public boolean isAccessibleRoom() {
        return blackUserId == null || blackUserId.equals(NOT_EXIST_VALUE);
    }

    public boolean isFullRoom() {
        return blackUserId != null && blackUserId > NOT_EXIST_VALUE;
    }

    public Long gameId() {
        return this.gameId;
    }

    public String name() {
        return this.name;
    }

    public Long whiteUserId() {
        return this.whiteUserId;
    }

    public Long blackUserId() {
        if (blackUserId == null) {
            return NOT_EXIST_VALUE;
        }
        return this.blackUserId;
    }
}
