package chess.dto.response;

import chess.domain.chesspiece.Color;

public class CurrentTurnDto {

    private final String name;
    private final Color currentTurn;

    private CurrentTurnDto(final String name, final Color currentTurn) {
        this.name = name;
        this.currentTurn = currentTurn;
    }

    public static CurrentTurnDto of(final String name, final Color currentTurn) {
        return new CurrentTurnDto(name, currentTurn);
    }

    public String getName() {
        return name;
    }

    public Color getCurrentTurn() {
        return currentTurn;
    }
}
