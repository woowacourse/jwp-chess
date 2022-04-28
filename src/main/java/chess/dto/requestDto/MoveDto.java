package chess.dto.requestDto;

public class MoveDto {

    private String start;
    private String target;

    protected MoveDto() {
    }

    public MoveDto(String start, String target) {
        this.start = start;
        this.target = target;
    }

    public String getStart() {
        return start;
    }

    public String getTarget() {
        return target;
    }
}
