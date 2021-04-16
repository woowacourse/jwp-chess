package chess.dto;

public class FinishDTO {

    private boolean isFinished;

    public FinishDTO(boolean isFinished) {
        this.isFinished = isFinished;
    }

    public boolean isFinished() {
        return isFinished;
    }
}
