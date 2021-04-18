package chess.dto.chess;

public class MoveRequestDto {

    private final String color;
    private final String source;
    private final String target;

    public MoveRequestDto(String color, String source, String target) {
        this.color = color;
        this.source = source;
        this.target = target;
    }

    public String getColor() {
        return color;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }
}
