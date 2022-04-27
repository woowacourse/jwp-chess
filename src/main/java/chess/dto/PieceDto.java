package chess.dto;

public class PieceDto {
    private final String position;
    private final String name;
    private final String teamColor;

    public PieceDto(final String name, final String position, final String teamColor) {
        this.name = name;
        this.position = position;
        this.teamColor = teamColor;
    }

    public String getPosition() {
        return position;
    }

    public String getTeam() {
        return teamColor;
    }

    public String getName() {
        return name;
    }

}
