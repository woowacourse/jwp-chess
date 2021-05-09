package chess.domain.room;

import chess.domain.Entity;

import java.util.Objects;

public class Room extends Entity<Long> {

    private final Long gameId;
    private final String name;
    private final Long whiteUserId;
    private Long blackUserId;

    public Room(Long gameId, String name, Long whiteUserId) {
        this(null, gameId, name, whiteUserId, null);
    }

    public Room(Long id, Long gameId, String name, Long whiteUserId) {
        this(id, gameId, name, whiteUserId, null);
    }

    public Room(Long gameId, String name, Long whiteUserId, Long blackUserId) {
        this(null, gameId, name, whiteUserId, blackUserId);
    }

    public Room(Long id, Long gameId, String name, Long whiteUserId, Long blackUserId) {
        super(id);
        this.gameId = gameId;
        this.name = name;
        this.whiteUserId = whiteUserId;
        this.blackUserId = blackUserId;
        validateRoom(name, whiteUserId);
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

    public void joinBlackUser(Long blackUserId) {
        validateBlackUserId(blackUserId);
        this.blackUserId = blackUserId;
    }

    private void validateBlackUserId(Long blackUserId) {
        Objects.requireNonNull(blackUserId, "Black User ID는 null일 수 없습니다.");
    }

    public boolean isWaitingRoom() {
        return blackUserId == null || blackUserId.equals(0L);
    }

    public boolean haveBlackUser() {
        return blackUserId != null && blackUserId > 0L;
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
        return this.blackUserId;
    }
}
