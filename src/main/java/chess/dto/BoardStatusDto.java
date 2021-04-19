package chess.dto;

import chess.domain.board.BoardStatus;

public class BoardStatusDto {
    private static final String SCORE = "";

    private String lastTurn;
    private String blackScore;
    private String whiteScore;

    public BoardStatusDto(BoardStatus boardStatus) {
        this.lastTurn = boardStatus.getLastTurn().name();
        this.blackScore = boardStatus.getBlackScore().getScore() + SCORE;
        this.whiteScore = boardStatus.getWhiteScore().getScore() + SCORE;
    }

    public String getLastTurn() {
        return lastTurn;
    }

    public String getBlackScore() {
        return blackScore;
    }

    public String getWhiteScore() {
        return whiteScore;
    }
}
