package chess.domain.dto;

public class TurnDto {
    private String turn;

    public TurnDto() {
    }

    private TurnDto(String turn) {
        this.turn = turn;
    }

    public static TurnDto of(String turn) {
        return new TurnDto(turn);
    }

    public String getTurn() {
        return turn;
    }
}
