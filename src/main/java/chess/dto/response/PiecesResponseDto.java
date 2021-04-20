package chess.dto.response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.position.Position;

public class PiecesResponseDto {

    private final Color winnerColor;
    private final boolean isPlaying;
    private final List<PieceResponseDto> alivePieces;

    public PiecesResponseDto(Color winnerColor, boolean isPlaying, Map<Position, Piece> board) {
        this.winnerColor = winnerColor;
        this.isPlaying = isPlaying;
        this.alivePieces = new ArrayList<>();
        for (Map.Entry<Position, Piece> piece : board.entrySet()) {
            alivePieces.add(new PieceResponseDto(piece.getKey().chessCoordinate(), piece.getValue().getName()));
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
