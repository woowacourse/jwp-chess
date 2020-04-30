package spring.dto;

import chess.game.ChessGame;

public class ChessGameDto {
    private BoardDto boardDto;
    private boolean turnIsBlack;
    private double whiteScore;
    private double blackScore;

    public ChessGameDto(ChessGame chessGame) {
        this.boardDto = new BoardDto(chessGame.getChessBoard());
        this.turnIsBlack = chessGame.getTurn().isBlack();
        this.whiteScore = chessGame.calculateScores().getWhiteScore().getValue();
        this.blackScore = chessGame.calculateScores().getBlackScore().getValue();
    }
}
