package chess.domain.game;

public enum State {
    RUNNING(true),
    READY(false),
    FINISHED(false);

    private final boolean disableOption;

    State(boolean disableOption) {
        this.disableOption = disableOption;
    }

    public boolean getDisableOption() {
        return disableOption;
    }
}
