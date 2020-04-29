package chess.model.domain.board;

import chess.dto.GameResultDto;
import chess.model.domain.piece.King;
import chess.model.domain.piece.Piece;
import chess.model.domain.piece.Team;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import util.NullChecker;

public class TeamScore {

    private static final double PAWN_SAME_FILE_SCORE = -0.5;
    private static final double ZERO = 0.0;

    private final Map<Team, Double> teamScore;

    public TeamScore(Collection<Piece> pieces, Map<Team, Integer> pawnSameFileCountByColor) {
        NullChecker.validateNotNull(pieces, pawnSameFileCountByColor);
        this.teamScore = Collections
            .unmodifiableMap(getTeamScore(pieces, pawnSameFileCountByColor));
    }

    public TeamScore(Map<Team, Double> teamScore) {
        NullChecker.validateNotNull(teamScore);
        this.teamScore = Collections.unmodifiableMap(new HashMap<>(teamScore));
    }

    private Map<Team, Double> getTeamScore(Collection<Piece> pieces,
        Map<Team, Integer> pawnSameFileByColor) {
        Map<Team, Double> teamScore = new HashMap<>();
        for (Team team : Team.values()) {
            double piecesSumScore = getSumScore(pieces, team);
            double pawnChargeScore = pawnSameFileByColor.get(team) * PAWN_SAME_FILE_SCORE;
            teamScore.put(team, piecesSumScore + pawnChargeScore);
        }
        return teamScore;
    }

    private double getSumScore(Collection<Piece> pieces, Team team) {
        boolean noKing = pieces.stream()
            .filter(piece -> piece.isSameTeam(team))
            .noneMatch(piece -> piece instanceof King);
        if (noKing) {
            return ZERO;
        }
        return pieces.stream()
            .filter(piece -> piece.isSameTeam(team))
            .mapToDouble(Piece::getScore)
            .sum();
    }

    public List<Team> getWinners() {
        return teamScore.keySet().stream()
            .filter(color -> teamScore.get(color) == getWinningScore())
            .collect(Collectors.toList());
    }

    private double getWinningScore() {
        return teamScore.values().stream()
            .max(Double::compareTo)
            .orElseThrow(IllegalAccessError::new);
    }

    public Map<Team, Double> getTeamScore() {
        return teamScore;
    }

    public double get(Team team) {
        NullChecker.validateNotNull(team);
        return teamScore.get(team);
    }

    public GameResultDto getGameResult(Team team) {
        NullChecker.validateNotNull(team);
        return new GameResultDto(getWinCount(team), getDrawCount(), getLoseCount(team));
    }

    private int getWinCount(Team team) {
        if (getWinners().size() == 1
            && getWinners().contains(team)) {
            return 1;
        }
        return 0;
    }

    private int getLoseCount(Team team) {
        if (getWinners().size() == 1
            && !getWinners().contains(team)) {
            return 1;
        }
        return 0;
    }

    private int getDrawCount() {
        if (getWinners().size() == 2) {
            return 1;
        }
        return 0;
    }
}
