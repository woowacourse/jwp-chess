package chess.web.dto;

import java.util.List;

public class ChessStatusDto {

    private final List<PieceDto> pieces;
    private final double blackScore;
    private final double whiteScore;

    public ChessStatusDto(List<PieceDto> pieces, double blackScore, double whiteScore) {
        this.pieces = pieces;
        this.blackScore = blackScore;
        this.whiteScore = whiteScore;
    }

    public List<PieceDto> getPieces() {
        return pieces;
    }

    public double getBlackScore() {
        return blackScore;
    }

    public double getWhiteScore() {
        return whiteScore;
    }
}
