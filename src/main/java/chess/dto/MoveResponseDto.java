package chess.dto;

public class MoveResponseDto {

    private final boolean finished;
    private final boolean success;
    private final String turn;

    public MoveResponseDto(boolean finished, boolean success, String turn) {
        this.finished = finished;
        this.success = success;
        this.turn = turn;
    }

    public boolean isFinished() {
        return finished;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getTurn() {
        return turn;
    }
}
