package chess.dto.response;

import java.util.ArrayList;
import java.util.List;

import chess.domain.piece.Color;
import chess.dto.PieceDto;
import chess.dto.PiecesDto;

public class PiecesResponseDto {

    private final Color winnerColor;
    private final boolean isPlaying;
    private final List<PieceResponseDto> alivePieces;

    public PiecesResponseDto(Color winnerColor, boolean isPlaying, PiecesDto piecesDtos) {
        this.winnerColor = winnerColor;
        this.isPlaying = isPlaying;
        this.alivePieces = new ArrayList<>();
        for (PieceDto movedPiece : piecesDtos.getPieceDtos()) {
            alivePieces.add(new PieceResponseDto(movedPiece));
        }
    }

    public PiecesResponseDto(PiecesResponsesDto piecesResponsesDto) {
        this.winnerColor = Color.NONE;
        this.isPlaying = true;
        this.alivePieces = piecesResponsesDto.getPieceResponseDtos();
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
