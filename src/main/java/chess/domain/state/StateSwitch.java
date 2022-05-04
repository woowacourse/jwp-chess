package chess.domain.state;

public enum StateSwitch {
    ON,
    OFF;

    public static StateSwitch invert(StateSwitch stateSwitch) {
        if (stateSwitch == ON) {
            return OFF;
        }
        return ON;
    }

    public boolean isOn(StateSwitch stateSwitch) {
        return stateSwitch == ON;
    }
}
