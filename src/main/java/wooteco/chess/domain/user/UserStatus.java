package wooteco.chess.domain.user;

import java.util.Arrays;

public enum UserStatus {
    Waiting("waiting"),
    Gaming("gaming");

    private final String name;

    UserStatus(final String name) {
        this.name = name;
    }

    public UserStatus of(String name) {
        return Arrays.stream(values())
                .filter(status -> status.name.equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("유저 상태가 없습니다."));
    }
}
