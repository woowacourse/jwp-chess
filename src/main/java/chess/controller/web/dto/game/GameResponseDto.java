package chess.controller.web.dto.game;

public class GameResponseDto {

    private Long id;
    private String turnOwner;
    private int turnNumber;
    private boolean isPlaying;
    private double whiteScore;
    private double blackScore;

    public GameResponseDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
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

    public double getWhiteScore() {
        return whiteScore;
    }

    public void setWhiteScore(final double whiteScore) {
        this.whiteScore = whiteScore;
    }

    public double getBlackScore() {
        return blackScore;
    }

    public void setBlackScore(final double blackScore) {
        this.blackScore = blackScore;
    }
}
