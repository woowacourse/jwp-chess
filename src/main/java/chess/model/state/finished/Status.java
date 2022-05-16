package chess.model.state.finished;

import chess.model.Team;
import chess.model.board.Board;
import chess.model.board.result.GameResult;
import java.util.Map;

public final class Status extends Finished {

    private final GameResult gameResult;

    public Status(Board board) {
        super(board);
        this.gameResult = new GameResult(board);
    }

    @Override
    public Map<String, Double> getScores() {
        return gameResult.getTeamScores();
    }

    @Override
    public String getWinner() {
        return gameResult.pickWinnerTeam();
    }

    @Override
    public String getSymbol() {
        return "STATUS";
    }
}
