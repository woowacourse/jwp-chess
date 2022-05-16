package chess.model.board.result;

import static chess.model.Team.BLACK;
import static chess.model.Team.WHITE;

import chess.model.Team;
import chess.model.board.Board;
import chess.model.position.Position;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.LongStream;

public class Score {

    private static final double PAWN_PENALTY_SCORE = -0.5;
    private static final int DUPLICATE_CHECK_PAWN_COUNT = 2;
    private static final int NO_PENALTY_PAWN_COUNT_ON_RANK = 1;

    private final Board board;

    public Score(final Board board) {
        this.board = board;
    }

    public Map<String, Double> teams() {
        Map<String, Double> scores = new HashMap<>();
        scores.put(BLACK.getName(), team(BLACK) + pawnPenalty(BLACK));
        scores.put(WHITE.getName(), team(WHITE) + pawnPenalty(WHITE));
        return scores;
    }

    public Double white() {
        return team(WHITE) + pawnPenalty(WHITE);
    }

    public Double black() {
        return team(BLACK) + pawnPenalty(BLACK);
    }

    private Double team(final Team team) {
        return board.calculateDefaultScoreFrom(team);
    }

    private Double pawnPenalty(final Team team) {
        List<Position> positionOfPawns = searchPositionOfPawns(team);
        return PAWN_PENALTY_SCORE * searchPenaltyPawns(positionOfPawns);
    }

    private List<Position> searchPositionOfPawns(final Team team) {
        return board.searchPositionOfPawnsFrom(team);
    }

    private long searchPenaltyPawns(final List<Position> positionOfPawns) {
        final long sameRankPawnCount = positionOfPawns.stream()
                .flatMapToLong(positionOfPawn -> calculateSameRankCount(positionOfPawn, positionOfPawns))
                .filter(count -> count > NO_PENALTY_PAWN_COUNT_ON_RANK)
                .sum() / DUPLICATE_CHECK_PAWN_COUNT;
        return sameRankPawnCount;
    }

    private LongStream calculateSameRankCount(Position position, List<Position> positionOfPawns) {
        return LongStream.of(positionOfPawns.stream()
                .filter(otherPosition -> otherPosition.isSameFile(position))
                .count());
    }
}
