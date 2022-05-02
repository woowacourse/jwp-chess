package chess.model.game;

import java.util.Arrays;

public enum Status {
    READY("READY"),
    PLAYING("PLAYING"),
    END("END");

    private final String name;

    Status(String name) {
        this.name = name;
    }

    public static Status findByGameEntity(String compareName) {
        return Arrays.stream(values())
                .filter(status -> status.name.equals(compareName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("알맞은 상태를 찾지 못했습니다."));
    }

    public boolean hasPlayed() {
        return this.equals(PLAYING) || this.equals(END);
    }

    public boolean isEnd() {
        return this.equals(END);
    }

    public boolean isPlaying() {
        return this.equals(PLAYING);
    }
}
