package chess.dto;

import java.util.Map;

import chess.model.Team;

public class ResultDto {

    private final Map<Team, Double> score;
    private final Team winner;

    public ResultDto(Map<Team, Double> score, Team winner) {
        this.score = score;
        this.winner = winner;
    }

    public Map<Team, Double> getScore() {
        return score;
    }

    public Team getWinner() {
        return winner;
    }
}
