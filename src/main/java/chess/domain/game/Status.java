package chess.domain.game;

public enum Status {
    READY("READY"),
    PLAYING("PLAYING"),
    END("END");

    private final String name;

    Status(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Status checkStatus() {
        return this;
    }
}
