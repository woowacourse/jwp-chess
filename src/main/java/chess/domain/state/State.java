package chess.domain.state;

public enum State {
    ON,
    OFF;

    public State invert(State state) {
        if (state == ON) {
            return OFF;
        }
        return ON;
    }
}
