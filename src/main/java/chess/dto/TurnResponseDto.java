package chess.dto;

import chess.domain.feature.Color;

public class TurnResponseDto {
    private final String turn;

    public TurnResponseDto(Color turn) {
        this.turn = turn.getColor();
    }

    public String getTurn() {
        return turn;
    }
}
