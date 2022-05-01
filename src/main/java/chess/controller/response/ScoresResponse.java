package chess.controller.response;

import chess.domain.Score;
import chess.domain.WinResult;
import chess.domain.piece.PieceColor;

import java.util.List;
import java.util.Map;

public class ScoresResponse {

    private final List<ScoreResponse> scores;
    private final String winnerName;

    private ScoresResponse(List<ScoreResponse> scores, String winnerName) {
        this.scores = scores;
        this.winnerName = winnerName;
    }

    public static ScoresResponse of(Map<PieceColor, Score> scores) {
        Score blackScore = scores.get(PieceColor.BLACK);
        Score whiteScore = scores.get(PieceColor.WHITE);
        List<ScoreResponse> scoreResponses = List.of(
                ScoreResponse.of(PieceColor.BLACK, blackScore),
                ScoreResponse.of(PieceColor.WHITE, whiteScore)
        );
        WinResult winResult = WinResult.of(blackScore, whiteScore);
        String winnerName = "";
        if (winResult != WinResult.DRAW) {
            winnerName = winResult.name();
        }
        return new ScoresResponse(scoreResponses, winnerName);
    }

    public List<ScoreResponse> getScores() {
        return scores;
    }

    public String getWinnerName() {
        return winnerName;
    }
}
