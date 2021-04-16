package chess.controller.web.dto;

import chess.chessgame.domain.piece.attribute.Color;

public class MoveResponseDto {
    private final boolean end;
    private final String nextColor;

    public MoveResponseDto(boolean end, Color nextColor) {
        this.end = end;
        this.nextColor = nextColor.name();
    }

    public boolean isEnd() {
        return end;
    }

    public String getNextColor() {
        return nextColor;
    }
}
