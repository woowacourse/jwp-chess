package chess.dto.response;

public class PieceResponseDto {

    private final String position;
    private final String name;

    public PieceResponseDto(String positionName, String pieceName) {
        this.position = positionName;
        this.name = pieceName;
    }

    public String getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }
}
