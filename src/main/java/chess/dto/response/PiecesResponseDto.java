package chess.dto.response;

import chess.domain.piece.Color;
import java.util.List;


public class PiecesResponseDto {

    private Color winnerColor;
    private boolean isPlaying;
    private List<PieceResponseDto> alivePieces;

    public PiecesResponseDto(List<PieceResponseDto> alivePieces) {
        this.winnerColor = Color.NONE;
        this.isPlaying = true;
        this.alivePieces = alivePieces;
    }

    public PiecesResponseDto(Color winnerColor, boolean isPlaying, List<PieceResponseDto> alivePieces) {
        this.winnerColor = winnerColor;
        this.isPlaying = isPlaying;
        this.alivePieces = alivePieces;
    }

    public Color getWinnerColor() {
        return winnerColor;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public List<PieceResponseDto> getAlivePieces() {
        return alivePieces;
    }
}
