package spring.dto;

public class ChessGameDto {
    private BoardDto boardDto;
    private boolean turnIsBlack;
    private double whiteScore;
    private double blackScore;

    public ChessGameDto(BoardDto boardDto, boolean turnIsBlack, double whiteScore, double blackScore) {
        this.boardDto = boardDto;
        this.turnIsBlack = turnIsBlack;
        this.whiteScore = whiteScore;
        this.blackScore = blackScore;
    }
}
