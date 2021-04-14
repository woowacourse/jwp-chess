package chess.dto;

public class BoardRequestDto {

    private String source;
    private String target;

    public BoardRequestDto(String source, String target) {
        this.source = source;
        this.target = target;
    }

    public String getTarget() {
        return target;
    }

    public String getSource() {
        return source;
    }

}
