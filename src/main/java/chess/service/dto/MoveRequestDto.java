package chess.service.dto;

public class MoveRequestDto {

    private String chessName;
    private String source;
    private String target;

    public MoveRequestDto(final String chessName, final String source, final String target) {
        this.chessName = chessName;
        this.source = source;
        this.target = target;
    }

    public String getChessName() {
        return chessName;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }

    public void setChessName(String chessName) {
        this.chessName = chessName;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
