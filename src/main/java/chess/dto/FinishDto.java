package chess.dto;

public class FinishDto {

    private final boolean isFinished;

    public FinishDto(final boolean isFinished) {
        this.isFinished = isFinished;
    }

    public boolean isFinished() {
        return isFinished;
    }
}
