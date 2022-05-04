package chess.domain.state;

public enum StateName {
    BLACK_TURN("BlackTurn", new BlackRunning()),
    WHITE_TURN("WhiteTurn", new WhiteRunning()),
    FINISH("Finish", new Finish());

    private final String value;
    private final State state;

    StateName(String value, State state) {
        this.value = value;
        this.state = state;
    }

    public boolean isSame(String value) {
        return this.value.equals(value);
    }

    public String getValue() {
        return value;
    }

    public State getState() {
        return state;
    }
}
