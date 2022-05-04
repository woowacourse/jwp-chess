package chess.dto;

public class GameNameAndTurnDto {

    private final String name;
    private final String turn;

    public GameNameAndTurnDto(String name, String turn) {
        this.name = name;
        this.turn = turn;
    }

    public String getName() {
        return name;
    }

    public String getTurn() {
        return turn;
    }
}
