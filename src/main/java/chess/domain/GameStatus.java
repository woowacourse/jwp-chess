package chess.domain;

import java.util.Arrays;

public enum GameStatus {
    END(0, "종료됨"),
    PLAYING(1, "진행중"),
    READY(2, "준비중");

    private final int value;
    private final String description;

    GameStatus(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public static String status(final int status) {
        return Arrays.stream(values())
                .filter(s -> s.value == status)
                .map(s -> s.description)
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }
}
