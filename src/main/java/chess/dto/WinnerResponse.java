package chess.dto;

public class WinnerResponse {

    private String winner;

    public WinnerResponse() {
        this(null);
    }

    public WinnerResponse(String winner) {
        this.winner = winner;
    }

    public String getWinner() {
        return winner;
    }
}
