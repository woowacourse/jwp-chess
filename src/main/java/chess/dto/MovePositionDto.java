package chess.dto;

public class MovePositionDto {
    private final String source;
    private final String target;

    public MovePositionDto(String source, String target) {
        this.source = source;
        this.target = target;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }
}
