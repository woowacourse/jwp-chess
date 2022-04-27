package chess.web.dto;

public class ChessStatusDto {

    private final int gameId;
    private final PiecesDto piecesDto;
    private final double blackScore;
    private final double whiteScore;

    public ChessStatusDto(int gameId, PiecesDto piecesDto, double blackScore, double whiteScore) {
        this.gameId = gameId;
        this.piecesDto = piecesDto;
        this.blackScore = blackScore;
        this.whiteScore = whiteScore;
    }

    public int getGameId() {
        return gameId;
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

