package chess.dao;

public class CommandResult {

    private final int effectedRowCount;

    public CommandResult(int effectedRowCount) {
        this.effectedRowCount = effectedRowCount;
    }

    public void throwOnNonEffected(String exceptionMessage) {
        if (effectedRowCount == 0) {
            throw new IllegalArgumentException(exceptionMessage);
        }
    }
}
