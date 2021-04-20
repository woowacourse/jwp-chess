package chess.service.dto;

public class MoveRequestDto {

    private String source;
    private String target;

    public MoveRequestDto() {
    }

    public MoveRequestDto(final String source, final String target) {
        this.source = source;
        this.target = target;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
