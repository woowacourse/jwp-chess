package chess.dto.chess;

public class MoveResponseDto {

    private final boolean finished;
    private final boolean success;

    public MoveResponseDto(final boolean finished, final boolean success) {
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
