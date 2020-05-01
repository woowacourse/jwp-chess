package chess.model.dto;

public class MoveDto {

    private String source;
    private String target;
    private Integer gameId;

    public MoveDto() {
    }

    public MoveDto(String source, String target, Integer gameId) {
        this.source = source;
        this.target = target;
        this.gameId = gameId;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }

    public Integer getGameId() {
        return gameId;
    }

    @Override
    public String toString() {
        return "MoveDto{" +
            "source='" + source + '\'' +
            ", target='" + target + '\'' +
            ", gameId=" + gameId +
            '}';
    }
}
