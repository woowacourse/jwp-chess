package chess.domain.game;

import chess.domain.Color;
import chess.domain.board.Board;
import chess.domain.board.Point;
import chess.domain.piece.Piece;
import chess.dto.Arguments;
import java.util.Map;

public abstract class GameState {

    protected Board board;
    protected State state;
    protected final Color turnColor;

    public GameState(Board board, Color turnColor, State state) {
        this.board = board;
        this.turnColor = turnColor;
        this.state = state;
    }

    public abstract GameState start();

    public abstract GameState finish();

    public abstract boolean isRunnable();

    public abstract GameState move(Arguments arguments);

    public Map<Point, Piece> getPointPieces() {
        return board.getPointPieces();
    }

    public State getState() {
        return state;
    }

    public String getColor() {
        return turnColor.name();
    }

    public Color getTurnColor() {
        return turnColor;
    }

    public Map<Color, Double> getColorScore() {
        return board.calculateScore();
    }
}
