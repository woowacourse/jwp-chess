package chess.dto;

import chess.model.piece.Team;

import java.util.Map;

public class ScoreDto {

    private static final String CANNOT_FOUND_WINNER_ERROR_MESSAGE = "우승자를 찾을 수 없습니다.";

    private final String winner;
    private final Map<String, Double> score;

    private ScoreDto(String winner, Map<String, Double> score) {
        this.winner = winner;
        this.score = score;
    }

    public static ScoreDto from(Map<String, Double> status) {
        String winner = findWinnerName(status);
        return new ScoreDto(winner, status);
    }

    private static String findWinnerName(Map<String, Double> status) {
        final double subtractedScore = Team.getPlayerTeams().stream()
                .mapToDouble(team -> status.get(team.name()))
                .reduce((x, y) -> x - y)
                .orElseThrow(() -> new IllegalArgumentException(CANNOT_FOUND_WINNER_ERROR_MESSAGE));
        return findWinner(subtractedScore);
    }

    private static String findWinner(final Double subtractedScore) {
        if (subtractedScore.equals(0.0)) {
            return Team.EMPTY.name();
        }
        if (subtractedScore < 0) {
            return Team.BLACK.name();
        }
        return Team.WHITE.name();
    }

    public String getWinner() {
        return winner;
    }

    public Map<String, Double> getScore() {
        return score;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        for (String team : score.keySet()) {
            stringBuilder.append("\"").append(team).append("\" : ").append(score.get(team)).append(",");
        }
        stringBuilder.append("\"").append("winner").append("\" : ").append("\"").append(winner).append("\"");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
