package wooteco.chess.dto;

import wooteco.chess.domain.Color;

import java.util.List;

public class GameManagerDTO {
    private final List<PieceResponseDTO> pieces;
    private final Color currentColor;
    private final boolean kingDead;
    private String errorMessage;

    public GameManagerDTO(final List<PieceResponseDTO> pieces, final Color currentColor, final boolean kingDead) {
        this.pieces = pieces;
        this.currentColor = currentColor;
        this.kingDead = kingDead;
    }

    public List<PieceResponseDTO> getPieces() {
        return pieces;
    }

    public Color getCurrentColor() {
        return currentColor;
    }

    public boolean isKingDead() {
        return kingDead;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
