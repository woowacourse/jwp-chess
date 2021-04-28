package chess.web.dto.game.move;

public class MoveCheckResponseDto {

    private final boolean movable;

    public MoveCheckResponseDto(final boolean movable) {
        this.movable = movable;
    }

    public boolean isMovable() {
        return movable;
    }
}
