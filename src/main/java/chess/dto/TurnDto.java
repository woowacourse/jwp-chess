package chess.dto;

import chess.domain.feature.Color;

public class TurnDto {
    private final String turn;

    public TurnDto(Color turn) {
        this.turn = turn.getColor();
    }

    public String getTurn() {
        return turn;
    }
}
