package chess.controller.web.dto.history;

public class HistoryResponseDto {

    private Long id;
    private Long gameId;
    private String source;
    private String target;
    private String turnOwner;
    private int turnNumber;
    private boolean isPlaying;

    public HistoryResponseDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(final Long gameId) {
        this.gameId = gameId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(final String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(final String target) {
        this.target = target;
    }

    public String getTurnOwner() {
        return turnOwner;
    }

    public void setTurnOwner(final String turnOwner) {
        this.turnOwner = turnOwner;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public void setTurnNumber(final int turnNumber) {
        this.turnNumber = turnNumber;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(final boolean playing) {
        isPlaying = playing;
    }
}
