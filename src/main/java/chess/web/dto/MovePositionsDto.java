package chess.web.dto;

public class MovePositionsDto {

    private final String source;
    private final String target;

    public MovePositionsDto(String source, String target) {
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
