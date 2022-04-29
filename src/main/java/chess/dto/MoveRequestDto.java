package chess.dto;

public class MoveRequestDto {
    private String source;
    private String target;

    public MoveRequestDto() {
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }
}
