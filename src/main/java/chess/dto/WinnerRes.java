package chess.dto;

public class WinnerRes {

    private String winner;

    public WinnerRes() {
        this(null);
    }

    public WinnerRes(String winner) {
        this.winner = winner;
    }

    public String getWinner() {
        return winner;
    }
}
