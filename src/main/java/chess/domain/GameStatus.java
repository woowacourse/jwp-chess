package chess.domain;

import java.util.Arrays;

public enum GameStatus {

    READY("ready"),
    PLAYING("playing"),
    FINISHED("finished");

    private final String name;

    GameStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static GameStatus find(String name) {
        return Arrays.stream(values())
                .filter(gameStatus -> name.equals(gameStatus.name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Status 를 찾을 수 없습니다."));
    }
}
