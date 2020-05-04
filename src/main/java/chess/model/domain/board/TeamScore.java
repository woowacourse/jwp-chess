package chess.model.domain.board;

import chess.model.domain.piece.King;
import chess.model.domain.piece.Pawn;
import chess.model.domain.piece.Piece;
import chess.model.domain.piece.Team;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import util.NullChecker;

public class TeamScore {

    private static final double PAWN_SAME_FILE_SCORE = 0.5;
    private static final double HAS_NOT_KING_SCORE = 0.0;
    private static final Integer NO_PAWN_COUNT = 0;

    private final Map<Team, Double> teamScore;

    private TeamScore(Map<Team, Double> teamScore) {
        this.teamScore = Collections.unmodifiableMap(new HashMap<>(teamScore));
    }

    public static TeamScore of(Map<Team, Double> teamScore) {
        NullChecker.validateNotNull(teamScore);
        return new TeamScore(teamScore);
    }

    public static TeamScore of(Collection<Piece> pieces,
        Map<Team, Integer> pawnSameFileCountByTeam) {
        NullChecker.validateNotNull(pieces, pawnSameFileCountByTeam);
        validationPawnCount(pieces, pawnSameFileCountByTeam);
        return new TeamScore(makeTeamScore(pieces, pawnSameFileCountByTeam));
    }

    private static void validationPawnCount(Collection<Piece> pieces,
        Map<Team, Integer> pawnSameFileCountByTeam) {
        for (Team team : Team.values()) {
            validationPawnCountByTeam(team, pieces, pawnSameFileCountByTeam);
        }
    }

    private static void validationPawnCountByTeam(Team team, Collection<Piece> pieces,
        Map<Team, Integer> pawnSameFileCountByTeam) {
        long pawnCount = calculatePawnCount(team, pieces);
        int pawnSameFileCount = pawnSameFileCountByTeam.getOrDefault(team, NO_PAWN_COUNT);
        validateMinimum(team, pawnSameFileCount);
        validateComparativeValue(team, pawnCount, pawnSameFileCount);
    }

    private static long calculatePawnCount(Team team, Collection<Piece> pieces) {
        return pieces.stream()
            .filter(piece -> piece.isSameTeam(team))
            .filter(piece -> piece instanceof Pawn)
            .count();
    }

    private static void validateMinimum(Team team, int pawnSameFileCount) {
        if (pawnSameFileCount == 1) {
            throw new IllegalArgumentException(team.getName() + "팀의 같은 파일 폰의 개수가 1개"
                + "입니다. 같은 파일의 폰은 최소 2개 이상이어야 합니다.");
        }
    }

    private static void validateComparativeValue(Team team, long pawnCount, int pawnSameFileCount) {
        if (pawnCount < pawnSameFileCount) {
            throw new IllegalArgumentException(team.getName() + "팀의 폰의 개수가 맞지 않습니다."
                + " 폰의 개수 : " + pawnCount + ", 같은 파일 폰의 개수 : " + pawnSameFileCount);
        }
    }

    private static Map<Team, Double> makeTeamScore(Collection<Piece> pieces,
        Map<Team, Integer> pawnSameFileByTeam) {
        return Arrays.stream(Team.values()).collect(Collectors
            .toMap(team -> team, team -> makeScore(team, pieces, pawnSameFileByTeam)));
    }

    private static double makeScore(Team team, Collection<Piece> pieces,
        Map<Team, Integer> pawnSameFileByTeam) {
        if (hasKing(team, pieces)) {
            return sumScore(pieces, team) - sumPawnSubtractWeight(team, pawnSameFileByTeam);
        }
        return HAS_NOT_KING_SCORE;
    }

    private static boolean hasKing(Team team, Collection<Piece> pieces) {
        return pieces.stream()
            .filter(piece -> piece.isSameTeam(team))
            .anyMatch(piece -> piece instanceof King);
    }

    private static double sumScore(Collection<Piece> pieces, Team team) {
        return pieces.stream()
            .filter(piece -> piece.isSameTeam(team))
            .mapToDouble(Piece::getScore)
            .sum();
    }

    private static double sumPawnSubtractWeight(Team team, Map<Team, Integer> pawnSameFileByTeam) {
        return pawnSameFileByTeam.getOrDefault(team, NO_PAWN_COUNT) * PAWN_SAME_FILE_SCORE;
    }

    public List<Team> findWinners() {
        return teamScore.keySet().stream()
            .filter(color -> teamScore.get(color) == findWinningScore())
            .collect(Collectors.toList());
    }

    private double findWinningScore() {
        return teamScore.values().stream()
            .max(Double::compareTo)
            .orElseThrow(IllegalAccessError::new);
    }

    public boolean isDraw() {
        return findWinners().size() == 2;
    }

    public boolean isNotDraw() {
        return !isDraw();
    }

    public boolean isWin(Team team) {
        NullChecker.validateNotNull(team);
        return isNotDraw() && findWinners().contains(team);
    }

    public boolean isLose(Team team) {
        NullChecker.validateNotNull(team);
        return isNotDraw() && !findWinners().contains(team);
    }

    public double findScore(Team team) {
        NullChecker.validateNotNull(team);
        return teamScore.get(team);
    }

    public Map<Team, Double> getTeamScore() {
        return teamScore;
    }
}
