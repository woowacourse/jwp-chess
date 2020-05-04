package wooteco.chess.dto;

import wooteco.chess.domain.piece.Color;

public class MoveStatusDto {
    private static final String EMPTY_STRING = "";

    private final boolean normalStatus;
    private final Color winner;
    private final String exception;

    public MoveStatusDto(boolean normalStatus, String exception) {
        this.normalStatus = normalStatus;
        this.winner = Color.NONE;
        this.exception = exception;
    }

    public MoveStatusDto(boolean normalStatus, Color winner) {
        this.normalStatus = normalStatus;
        this.winner = winner;
        this.exception = EMPTY_STRING;
    }

    public MoveStatusDto(boolean normalStatus) {
        this.normalStatus = normalStatus;
        this.winner = Color.NONE;
        this.exception = EMPTY_STRING;
    }

    public boolean getNormalStatus() {
        return normalStatus;
    }

    public Color getWinner() {
        return winner;
    }

    public String getException() {
        return this.exception;
    }
}
