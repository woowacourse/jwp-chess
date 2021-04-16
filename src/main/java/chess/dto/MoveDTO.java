package chess.dto;

public class MoveDTO {

    private String source;
    private String target;

    public MoveDTO() {

    }

    public MoveDTO(String source, String target) {
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
