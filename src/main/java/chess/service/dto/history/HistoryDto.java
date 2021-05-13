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

    public HistoryDto(final Long gameId, final String source, final String target, final String turnOwner,
                      final int turnNumber, final boolean isPlaying) {
        this.gameId = gameId;
        this.source = source;
        this.target = target;
        this.turnOwner = turnOwner;
        this.turnNumber = turnNumber;
        this.isPlaying = isPlaying;
    }

    public HistoryDto(final Long id, final Long gameId, final String source, final String target,
                      final String turnOwner, final int turnNumber, final boolean isPlaying) {
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
