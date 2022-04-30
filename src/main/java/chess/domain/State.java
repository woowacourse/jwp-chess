package chess.domain;

public enum State {

    RUN("run"),
    END("end");

    private final String value;

    State(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
