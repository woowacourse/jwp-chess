package chess.dto;

public class MoveResponseDto {

    private final boolean finished;
    private final boolean success;

    public MoveResponseDto(boolean finished, boolean success) {
        this.finished = finished;
        this.success = success;
    }

    public boolean isFinished() {
        return finished;
    }

    public boolean isSuccess() {
        return success;
    }
}
