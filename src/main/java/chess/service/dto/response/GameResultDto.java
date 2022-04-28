package chess.service.dto.response;

import static java.util.stream.Collectors.toMap;

import chess.model.Color;
import chess.model.game.GameResult;
import java.util.Map;

public class GameResultDto {
    private final Map<String, Double> playerPoints;
    private final String winnerColor;
    private final boolean isDraw;

    public GameResultDto(Map<String, Double> playerPoints, String winnerColor, boolean isDraw) {
        this.playerPoints = playerPoints;
        this.winnerColor = winnerColor;
        this.isDraw = isDraw;
    }

    public static GameResultDto of(GameResult result) {
        Map<String, Double> score = convert(result.getScore());
        String winnerName = result.getWinnerColor().name();
        boolean isDraw = winnerName.equals("NOTHING");
        return new GameResultDto(score, winnerName, isDraw);
    }

    private static Map<String, Double> convert(Map<Color, Double> colorScores) {
        return colorScores
                .keySet().stream()
                .collect(toMap(Enum::name, colorScores::get));
    }

    public Map<String, Double> getPlayerPoints() {
        return playerPoints;
    }

    public String getWinnerColor() {
        return winnerColor;
    }

    public boolean getIsDraw() {
        return isDraw;
    }
}
