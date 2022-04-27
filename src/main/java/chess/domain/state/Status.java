package chess.domain.state;

import chess.domain.chessboard.Board;
import chess.domain.game.Player;
import chess.domain.game.Score;

import java.util.HashMap;

public final class Status extends Finished {

    public Status(final Board board) {
        super(board);
    }

    @Override
    public State proceed(final String command) {
        throw new IllegalArgumentException("[ERROR] 올바른 명령을 입력해주세요.");
    }

    @Override
    public boolean isStatus() {
        return true;
    }

    public HashMap<Player, Double> calculateScore() {
        final Score score = new Score();
        score.calculateScore(board);
        return score.getScores();
    }

}
