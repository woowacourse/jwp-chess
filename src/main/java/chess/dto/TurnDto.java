package chess.dto;

import chess.domain.piece.Color;

public class TurnDto {

    private final Color turn;

    public TurnDto(final String turn) {
        this.turn = Color.of(turn);
    }

    public Color getTurn() {
        return turn;
    }
}
