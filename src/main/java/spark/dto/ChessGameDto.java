package spark.dto;

public class ChessGameDto {
    private BoardDto boardDto;
    private int turnIsBlack;
    private double whiteScore;
    private double blackScore;

    public ChessGameDto(BoardDto boardDto, int turnIsBlack, double whiteScore, double blackScore) {
        this.boardDto = boardDto;
        this.turnIsBlack = turnIsBlack;
        this.whiteScore = whiteScore;
        this.blackScore = blackScore;
    }
}
