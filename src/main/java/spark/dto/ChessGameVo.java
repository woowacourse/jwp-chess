package spark.dto;

public class ChessGameVo {
    private final int id;
    private final String whiteName;
    private final String blackName;
    private final boolean turnIsBlack;

    public ChessGameVo(int id, String whiteName, String blackName, int turnIsBlack) {
        this.id = id;
        this.whiteName = whiteName;
        this.blackName = blackName;
        this.turnIsBlack = convertIntToBoolean(turnIsBlack);
    }

    private static boolean convertIntToBoolean(int value) {
        return value == 1;
    }

    public boolean isTurnBlack() {
        return turnIsBlack;
    }
}
