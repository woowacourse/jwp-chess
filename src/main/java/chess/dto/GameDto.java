package chess.dto;

import chess.domain.board.Board;

import java.util.Map;

public class GameDto {

    private final Map<String, String> board;
    private final double blackScore;
    private final double whiteScore;

    public GameDto(Board board, double blackScore, double whiteScore) {
        this(board.parseUnicodeBoardAsMap(), blackScore, whiteScore);
    }

    public GameDto(Map<String, String> board, double blackScore, double whiteScore) {
        this.board = board;
        this.blackScore = blackScore;
        this.whiteScore = whiteScore;
    }

    public Map<String, String> getBoard() {
        return board;
    }

    public double getBlackScore() {
        return blackScore;
    }

    public double getWhiteScore() {
        return whiteScore;
    }
}
