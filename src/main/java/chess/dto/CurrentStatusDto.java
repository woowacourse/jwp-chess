package chess.dto;

import chess.domain.CurrentStatus;

public class CurrentStatusDto {
    private final String state;
    private final String turn;

    public CurrentStatusDto(CurrentStatus currentStatus) {
        this.state = currentStatus.getStateToString();
        this.turn = currentStatus.getTurnToString();
    }

    public String getState() {
        return state;
    }

    public String getTurn() {
        return turn;
    }
}
