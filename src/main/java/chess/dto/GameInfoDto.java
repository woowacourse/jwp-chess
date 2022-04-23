package chess.dto;

public class GameInfoDto {
    private final String state;
    private final String turn;

    public GameInfoDto(String state, String turn) {
        this.state = state;
        this.turn = turn;
    }

    public String getState() {
        return state;
    }

    public String getTurn() {
        return turn;
    }
}
