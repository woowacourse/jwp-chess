package chess.controller.dto;

import chess.domain.board.Board;

public class GameInfoDto {
    private final String[][] board;
    private final double blackScore;
    private final double whiteScore;

    public GameInfoDto(String[][] board, double blackScore, double whiteScore) {
        this.board = board;
        this.blackScore = blackScore;
        this.whiteScore = whiteScore;
    }

    public GameInfoDto(Board board, double blackScore, double whiteScore) {
        this(board.parseUnicodeBoard(), blackScore, whiteScore);
    }

    public String[][] getBoard() {
        return board;
    }

    public double getBlackScore() {
        return blackScore;
    }

    public double getWhiteScore() {
        return whiteScore;
    }
}
