package chess.dto.request;

public class PiecesRequestDto {

    private String source;
    private String target;

    public PiecesRequestDto(String source, String target) {
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
