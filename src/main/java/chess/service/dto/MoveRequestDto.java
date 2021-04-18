package chess.service.dto;

public class MoveRequestDto {

    private String chessName;
    private String source;
    private String target;

    public MoveRequestDto() {
    }

    public MoveRequestDto(final String chessName, final String source, final String target) {
        this.chessName = chessName;
        this.source = source;
        this.target = target;
    }

    public String getChessName() {
        return chessName;
    }

    public void setChessName(String chessName) {
        this.chessName = chessName;
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
