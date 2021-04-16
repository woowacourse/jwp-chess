package chess.dto;

import chess.domain.board.Board;
import chess.domain.piece.PieceColor;
import chess.domain.result.Result;
import java.util.LinkedHashMap;
import java.util.Map;

public class ScoreDto {

    private final Result result;
    private final Map<PieceColor, Double> scores;

    public ScoreDto(Board board) {
        this.result = new Result(board);
        this.scores = new LinkedHashMap<>();
        putScores();
    }

    private void putScores() {
        scores.put(PieceColor.WHITE, result.calculateTotalScore(PieceColor.WHITE).getScore());
        scores.put(PieceColor.BLACK, result.calculateTotalScore(PieceColor.BLACK).getScore());
    }

    public Map<PieceColor, Double> getScores() {
        return scores;
    }

}
