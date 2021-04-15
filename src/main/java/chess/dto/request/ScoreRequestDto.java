package chess.dto.request;

import chess.domain.piece.Color;

public class ScoreRequestDto {

    private Color color;

    public ScoreRequestDto(String colorName) {
        this.color = Color.valueOf(colorName);
    }

    public Color getColor() {
        return color;
    }
}
