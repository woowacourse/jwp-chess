package chess.turndecider;

import chess.domain.piece.Piece;
import chess.domain.piece.PieceTeam;
import chess.turndecider.state.State;
import chess.turndecider.state.WhiteTeam;

public class AlternatingGameFlow implements GameFlow {

    private State currentState = new WhiteTeam();

    @Override
    public boolean isCorrectTurn(Piece sourcePiece) {
        return isSameColor(sourcePiece);
    }

    @Override
    public void nextState(boolean isGameFinished) {
        currentState = currentState.nextState(isGameFinished);
    }

    private boolean isSameColor(Piece sourcePiece) {
        return currentState.isSameColor(sourcePiece);
    }

    @Override
    public boolean isRunning() {
        return currentState.isRunning();
    }

    @Override
    public State currentState() {
        return currentState;
    }

    @Override
    public PieceTeam currentPieceTeam() {
        return currentState.pieceTeam();
    }
}
