package chess.domain;

public enum UserStatus {
    READY, IN_GAME;

    public static UserStatus of(Long roomId) {
        if (roomId == 0) {
            return UserStatus.READY;
        }

        return UserStatus.IN_GAME;
    }

}
