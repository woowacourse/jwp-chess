package chess.dto;

public class ResultResponse {
    private final String winner;
    private final boolean tie;

    public ResultResponse(String winner, boolean tie) {
        this.winner = winner;
        this.tie = tie;
    }

    public String getWinner() {
        return winner;
    }

    public boolean isTie() {
        return tie;
    }
}
