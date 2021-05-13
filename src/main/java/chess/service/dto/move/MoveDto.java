package chess.service.dto.move;

public class MoveDto {
    private String source;
    private String target;

    public MoveDto() {
    }

    public String getSource() {
        return source;
    }

    public void setSource(final String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(final String target) {
        this.target = target;
    }
}
