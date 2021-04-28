package chess.web.dto.game.move;

public class MoveResponseDto {

    private final boolean finished;

    public MoveResponseDto(final boolean finished) {
        this.finished = finished;
    }

    public boolean isFinished() {
        return finished;
    }

}
