package chess.controller.web.dto.state;

public class StateResponseDto {

    private String turnOwner;
    private int turnNumber;
    private boolean isPlaying;

    public StateResponseDto() {
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
