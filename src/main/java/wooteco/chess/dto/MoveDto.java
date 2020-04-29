package wooteco.chess.dto;

public class MoveDto {
    private String source;
    private String target;

    protected MoveDto() {
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }
}
