package chess.domain;

import chess.domain.board.Position;
import chess.domain.gamestate.Ready;
import chess.domain.gamestate.Score;
import chess.domain.gamestate.State;
import chess.domain.piece.Piece;
import java.util.Map;
import java.util.Objects;

public class ChessGame {
    private State state;
    private String title;
    private String password;

    public ChessGame() {
        this.state = new Ready();
    }

    public ChessGame(String title, String password) {
        this.state = new Ready();
        this.title = title;
        this.password = password;
    }

    public void start() {
        this.state = this.state.start();
    }

    public void load(Map<Position, Piece> board, boolean whiteTurn) {
        this.state = this.state.load(board, whiteTurn);
    }

    public void move(Position sourcePosition, Position targetPosition) {
        this.state = this.state.move(sourcePosition, targetPosition);
    }

    public void end() {
        this.state = this.state.end();
    }

    public boolean isFinished() {
        return this.state.isFinished();
    }

    public boolean incorrectPassword(String password) {
        return !this.password.equals(password);
    }

    public Map<Position, Piece> getBoardSquares() {
        return this.state.getBoard().getSquares();
    }

    public Map<Camp, Score> getScores() {
        return this.state.getScores();
    }

    public Camp getWinner() {
        return this.state.getWinner();
    }

    public String getTitle() {
        return title;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(title, chessGame.title) && Objects.equals(password, chessGame.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, password);
    }
}
