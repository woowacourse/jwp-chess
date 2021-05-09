package chess.service.dto.history;

import chess.domain.history.History;

public class HistoryDto {

    private Long id;
    private Long gameId;
    private String source;
    private String target;
    private String turnOwner;
    private int turnNumber;
    private boolean isPlaying;

    public HistoryDto() {
    }

    public HistoryDto(Long gameId, String source, String target, String turnOwner, int turnNumber, boolean isPlaying) {
        this.gameId = gameId;
        this.source = source;
        this.target = target;
        this.turnOwner = turnOwner;
        this.turnNumber = turnNumber;
        this.isPlaying = isPlaying;
    }

    public HistoryDto(Long id, Long gameId, String source, String target, String turnOwner, int turnNumber, boolean isPlaying) {
        this.id = id;
        this.gameId = gameId;
        this.source = source;
        this.target = target;
        this.turnOwner = turnOwner;
        this.turnNumber = turnNumber;
        this.isPlaying = isPlaying;
    }

    public static HistoryDto from(final History history) {
        return new HistoryDto(
                history.gameId(),
                history.sourceToString(),
                history.targetToString(),
                history.turnOwnerName(),
                history.turnNumber(),
                history.isPlaying()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
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

    public String getTurnOwner() {
        return turnOwner;
    }

    public void setTurnOwner(String turnOwner) {
        this.turnOwner = turnOwner;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public void setTurnNumber(int turnNumber) {
        this.turnNumber = turnNumber;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }
}
