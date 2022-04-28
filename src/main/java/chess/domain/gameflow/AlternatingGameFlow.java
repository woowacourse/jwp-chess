package chess.domain.gameflow;

import chess.domain.gameflow.state.WhiteTeam;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceTeam;
import chess.domain.gameflow.state.State;

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
    public String currentStateName() {
        return currentState.name();
    }

    @Override
    public PieceTeam currentPieceTeam() {
        return currentState.pieceTeam();
    }
}
