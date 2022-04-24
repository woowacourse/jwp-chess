package chess.entity;

public class SquareEntity {
    private final String position;
    private final String team;
    private final String symbol;

    public SquareEntity(String position, String team, String symbol) {
        this.position = position;
        this.team = team;
        this.symbol = symbol;
    }

    public String getPosition() {
        return position;
    }

    public String getTeam() {
        return team;
    }

    public String getSymbol() {
        return symbol;
    }
}
