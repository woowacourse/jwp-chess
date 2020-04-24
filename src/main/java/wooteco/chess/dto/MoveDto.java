package wooteco.chess.dto;

public class MoveDto {
    private String source;
    private String target;

    protected MoveDto() {

    }

    public MoveDto(String source, String target) {
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
