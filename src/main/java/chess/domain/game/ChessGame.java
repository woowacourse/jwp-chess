package chess.domain.game;

import chess.domain.board.Board;
import chess.domain.game.state.State;
import chess.domain.game.state.idle.Ready;
import chess.domain.game.state.running.BlackTurn;
import chess.domain.piece.Position;

import java.util.Optional;

public class ChessGame {

    private final Long gameId;
    private final Board board;
    private State state;

    public ChessGame(Long gameId, Board board) {
        this.gameId = gameId;
        this.board = board;
        this.state = new Ready(this);
    }

    public void changeState(State state) {
        this.state = state;
    }

    public void move(Position source, Position target) {
        state.move(source, target);
    }

    public void end() {
        state.end();
    }

    public void start() {
        state.start();
    }

    public boolean isKingCaught() {
        return board.isKingCaught();
    }

    public Board getBoard() {
        return board;
    }

    public double getWhiteScore() {
        return board.getWhiteScore();
    }

    public double getBlackScore() {
        return board.getBlackScore();
    }

    public Optional<String> getWinnerColorNotation() {
        return state.getWinnerColorNotation();
    }

    public void moveWhitePiece(Position source, Position target) {
        board.moveWhitePiece(source, target);
    }

    public void moveBlackPiece(Position source, Position target) {
        board.moveBlackPiece(source, target);
    }

    public void catchBlackPiece() {
        board.catchBlackPiece();
    }

    public void catchWhitePiece() {
        board.catchWhitePiece();
    }

    public String getStatus() {
        return state.getStatus();
    }

    public State getState() {
        return state;
    }

    public Long getGameId() {
        return gameId;
    }

    public boolean isBlackTurn() {
        return state instanceof BlackTurn;
    }

}
