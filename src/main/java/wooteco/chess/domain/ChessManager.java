package wooteco.chess.domain;

import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.piece.Team;

import java.util.Optional;

public class ChessManager {

    private ChessRunner chessRunner;
    private boolean playing;

    public void start() {
        this.chessRunner = new ChessRunner();
        playing = true;
    }

    public void end() {
        playing = false;
    }

    public void move(String source, String target) {
        chessRunner.update(source, target);
        playing = stopGameIfWinnerExists();
    }

    private boolean stopGameIfWinnerExists() {
        return !chessRunner.findWinner().isPresent();
    }

    public boolean isPlaying() {
        return playing;
    }

    public double calculateScore() {
        return chessRunner.calculateScore();
    }

    public Board getBoard() {
        return chessRunner.getBoard();
    }

    public Team getCurrentTeam() {
        return chessRunner.getCurrentTeam();
    }

    public Optional<Team> getWinner() {
        return chessRunner.findWinner();
    }
}
