package chess.web.dto.game.move;

public class MoveCheckResponseDto {

    private boolean movable;

    public MoveCheckResponseDto() {
    }

    public MoveCheckResponseDto(final boolean movable) {
        this.movable = movable;
    }

    public boolean isMovable() {
        return movable;
    }

}
