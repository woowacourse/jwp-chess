package chess.dto;

import chess.domain.piece.Color;

public class TurnDTO {

    private final Color turn;

    public TurnDTO(final String turn) {
        this.turn = Color.of(turn);
    }

    public Color getTurn() {
        return turn;
    }
}
