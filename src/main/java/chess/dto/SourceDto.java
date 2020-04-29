package chess.dto;

public class SourceDto {

    private Integer gameId;
    private String source;

    public SourceDto() {
    }

    public SourceDto(Integer gameId, String source) {
        this.gameId = gameId;
        this.source = source;
    }

    public Integer getGameId() {
        return gameId;
    }

    public String getSource() {
        return source;
    }
}
