package chess.dto;

public class MoveCommandDto {
    private String source;
    private String target;

    public MoveCommandDto(){
    }

    public MoveCommandDto(String source, String target) {
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
