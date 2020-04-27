package wooteco.chess.domain.judge;

import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.piece.Team;

import java.util.Optional;

public class Judge {

    private static final double PAWN_DUPLICATED_DEDUCTION = 0.5;

    public double getScoreByTeam(final Board board, final Team team) {
        double defaultScore = board.getDefaultScore(team);
        double deduction = board.countDuplicatedPawns(team) * PAWN_DUPLICATED_DEDUCTION;
        return defaultScore - deduction;
    }

    public Optional<Team> findWinner(final Board board) {
        if (board.hasKing(Team.BLACK) && board.hasKing(Team.WHITE)) {
            return Optional.empty();
        }
        if (board.hasKing(Team.BLACK)) {
            return Optional.of(Team.BLACK);
        }
        return Optional.of(Team.WHITE);
    }
}
