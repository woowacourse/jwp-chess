package chess.controller.response;

import chess.domain.Score;
import chess.domain.WinResult;
import chess.domain.piece.PieceColor;
import chess.dto.ScoreDto;

import java.util.List;
import java.util.Map;

public class ScoresResponse {

    private final List<ScoreDto> scores;
    private final String winnerName;

    private ScoresResponse(List<ScoreDto> scores, String winnerName) {
        this.scores = scores;
        this.winnerName = winnerName;
    }

    public static ScoresResponse of(Map<PieceColor, Score> scores) {
        Score blackScore = scores.get(PieceColor.BLACK);
        Score whiteScore = scores.get(PieceColor.WHITE);
        List<ScoreDto> scoreDtos = List.of(
                ScoreDto.of(PieceColor.BLACK, blackScore),
                ScoreDto.of(PieceColor.WHITE, whiteScore)
        );
        WinResult winResult = WinResult.of(blackScore, whiteScore);
        String winnerName = "";
        if (winResult != WinResult.DRAW) {
            winnerName = winResult.name();
        }
        return new ScoresResponse(scoreDtos, winnerName);
    }

    public List<ScoreDto> getScores() {
        return scores;
    }

    public String getWinnerName() {
        return winnerName;
    }
}
