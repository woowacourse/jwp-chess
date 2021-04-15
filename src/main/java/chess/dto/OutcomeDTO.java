package chess.dto;

import chess.domain.game.Result;

public class OutcomeDTO {
    private final String blackOutcome;
    private final String whiteOutcome;

    public OutcomeDTO(Result result) {
        this.blackOutcome = result.getBlackOutcome();
        this.whiteOutcome = result.getWhiteOutcome();
    }

    public String getBlackOutcome() {
        return blackOutcome;
    }

    public String getWhiteOutcome() {
        return whiteOutcome;
    }
}
