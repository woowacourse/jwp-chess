package chess.web.dto;

public class ChessStatusDto {

    private final PiecesDto piecesDto;
    private final double blackScore;
    private final double whiteScore;

    public ChessStatusDto(PiecesDto piecesDto, double blackScore, double whiteScore) {
        this.piecesDto = piecesDto;
        this.blackScore = blackScore;
        this.whiteScore = whiteScore;
    }

    public PiecesDto getPiecesDto() {
        return piecesDto;
    }

    public double getBlackScore() {
        return blackScore;
    }

    public double getWhiteScore() {
        return whiteScore;
    }
}
