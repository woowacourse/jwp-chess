package wooteco.chess.domain.score;

import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.Team;
import wooteco.chess.domain.position.File;
import wooteco.chess.domain.position.Position;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Score {
    private static final int PAWN_SCORE_THRESHOLD = 2;
    private static final double PAWN_SCORE_IN_SAME_FILE = 0.5d;

    private Score() {

    }

    public static double calculateScore(Map<Position, Piece> board, Team team) {
        double totalScore = calculateTotalScore(board, team);
        return calculatePawnScore(board, team, totalScore);
    }

    private static double calculateTotalScore(Map<Position, Piece> board, Team team) {
        return board.values().stream()
                .filter(piece -> team.isSameTeamWith(piece.getTeam()))
                .mapToDouble(Piece::getScore)
                .sum();
    }

    private static double calculatePawnScore(Map<Position, Piece> board, Team team, double score) {
        for (File file : File.values()) {
            List<Map.Entry<Position, Piece>> sameFile = board.entrySet().stream()
                    .filter(entry -> File.of(entry.getKey().getFile()).equals(file))
                    .filter(entry -> entry.getValue().isPawn() && !entry.getValue().isEnemy(team))
                    .collect(Collectors.toList());

            if (sameFile.size() >= PAWN_SCORE_THRESHOLD) {
                score -= sameFile.size() * PAWN_SCORE_IN_SAME_FILE;
            }
        }
        return score;
    }
}
