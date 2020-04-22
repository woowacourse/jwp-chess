package wooteco.chess.domain.judge;

import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.piece.Team;

import java.util.Optional;

public class Judge {
    public double getScoreByTeam(Board board, Team team) {
        return board.getDefaultScore(team) - board.countDuplicatedPawns(team) * 0.5;
    }

    public Optional<Team> findWinner(Board board) {
        if (board.hasKing(Team.BLACK) && board.hasKing(Team.WHITE)) {
            return Optional.empty();
        }
        if (board.hasKing(Team.BLACK)) {
            return Optional.of(Team.BLACK);
        }
        return Optional.of(Team.WHITE);
    }
}
