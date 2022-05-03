package chess.dto.response;

public class GameStatusDto {

    private final boolean finished;

    public GameStatusDto(boolean finished) {
        this.finished = finished;
    }

    public boolean isFinished() {
        return finished;
    }
}
