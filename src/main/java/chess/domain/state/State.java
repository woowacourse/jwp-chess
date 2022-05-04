package chess.domain.state;

import chess.domain.board.Board;
import chess.domain.piece.Color;
import chess.domain.position.Position;
import chess.domain.score.ChessScore;
import chess.domain.score.ScoreCalculator;

public class State {

    private static final String CANNOT_MOVE_OPPONENT_PIECE = "상대방의 말을 움직일 수 없습니다.";
    private Color color;
    private StateSwitch stateSwitch;
    private final Board board;

    public State(Color color, StateSwitch stateSwitch, Board board) {
        this.color = color;
        this.stateSwitch = stateSwitch;
        this.board = board;
    }

    public State(Color color, StateSwitch stateSwitch) {
        this.color = color;
        this.stateSwitch = stateSwitch;
        this.board = new Board();
    }

    private void validateMoveOpponentPiece(Color color, Position fromPosition) {
        if (board.findPiece(fromPosition).isSameColor(color.invert())) {
            throw new IllegalArgumentException(CANNOT_MOVE_OPPONENT_PIECE);
        }
    }

    public void movePiece(Position fromPosition, Position toPosition, Color color) {
        validateMoveOpponentPiece(color, fromPosition);
        board.movePiece(fromPosition, toPosition);
    }

    public boolean isKingDead() {
        return board.isKingNotExist(this.color.invert());
    }

    public ChessScore calculateScore() {
        return ScoreCalculator.calculateChessScore(board.getPieces());
    }

    public void changeColor() {
        this.color = this.color.invert();
    }

    public void changeSwitch() {
        this.stateSwitch = StateSwitch.invert(this.stateSwitch);
    }

    public Color getColor() {
        return color;
    }

    public StateSwitch getStateSwitch() {
        return stateSwitch;
    }

    public Board getBoard() {
        return board;
    }
}
