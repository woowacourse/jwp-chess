package chess.controller.dto;

import chess.domain.board.Board;
import chess.domain.board.position.Position;

import java.util.Map;

public class GameInfoDto {

    private final Map<String, String> board;
    private final double blackScore;
    private final double whiteScore;

    public GameInfoDto(Board board, double blackScore, double whiteScore) {
        this(board.parseUnicodeBoardAsMap(), blackScore, whiteScore);
    }

    public GameInfoDto(Map<String, String> board, double blackScore, double whiteScore) {
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

    @Override
    public String toString() {
        return board.toString();
    }
}
