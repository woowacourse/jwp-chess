package chess.controller.web.dto.history;

public class HistoryResponseDto {

    private String moveCommand;
    private String turnOwner;
    private int turnNumber;
    private boolean isPlaying;

    public HistoryResponseDto() {
    }

    public String getMoveCommand() {
        return moveCommand;
    }

    public void setMoveCommand(String moveCommand) {
        this.moveCommand = moveCommand;
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
