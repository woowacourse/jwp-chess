package chess.model.board.result;

import chess.model.Team;
import chess.model.board.Board;
import chess.model.piece.Piece;
import chess.model.position.Position;
import java.util.Map;

public class GameResult {

    private final Score score;
    private final Winner winner;

    public GameResult(Board board) {
        this.score = new Score(board);
        this.winner = new Winner(score);
    }

    public Map<Team, Double> getTeamScores() {
        return score.teams();
    }

    public Team pickWinnerTeam() {
        return winner.team();
    }
}
