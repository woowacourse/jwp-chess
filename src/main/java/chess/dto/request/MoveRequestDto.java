package chess.dto.request;

public class MoveRequestDto {

    private String source;
    private String target;

    private MoveRequestDto() {
    }

    public MoveRequestDto(final String source, final String target) {
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
