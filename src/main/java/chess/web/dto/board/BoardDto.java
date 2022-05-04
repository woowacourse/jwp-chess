package chess.web.dto.board;

public class BoardDto {

    private final int gameId;
    private final PiecesDto piecesDto;
    private final double blackScore;
    private final double whiteScore;

    public BoardDto(int gameId, PiecesDto piecesDto, double blackScore, double whiteScore) {
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

