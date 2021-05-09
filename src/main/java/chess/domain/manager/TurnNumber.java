package chess.domain.manager;

public class TurnNumber {

    private final static TurnNumber FIRST_TURN_NUMBER = new TurnNumber(1);

    private final int value;

    private TurnNumber(int value) {
        this.value = value;
    }

    public static TurnNumber of(int number) {
        return new TurnNumber(number);
    }

    public static TurnNumber getFirstTurnNumber() {
        return FIRST_TURN_NUMBER;
    }

    public TurnNumber increaseNumber() {
        return new TurnNumber(this.value + 1);
    }

    public int toInt() {
        return this.value;
    }
}
