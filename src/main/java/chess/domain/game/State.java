package chess.domain.game;

public enum State {
    RUNNING("disabled"),
    READY(""),
    FINISHED("");

    private final String disableOption;

    State(String disableOption) {
        this.disableOption = disableOption;
    }

    public String getDisableOption() {
        return disableOption;
    }
}
