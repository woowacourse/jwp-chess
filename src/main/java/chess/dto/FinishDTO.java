package chess.dto;

public class FinishDTO {

    private final boolean isFinished;

    public FinishDTO(final boolean isFinished) {
        this.isFinished = isFinished;
    }

    public boolean isFinished() {
        return isFinished;
    }
}
