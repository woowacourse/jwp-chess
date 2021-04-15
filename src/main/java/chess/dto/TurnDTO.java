package chess.dto;

public class TurnDTO {
    private String turn;

    public TurnDTO() {
    }

    public TurnDTO(String turn) {
        this.turn = turn;
    }

    public String getTurn() {
        return turn;
    }
}
