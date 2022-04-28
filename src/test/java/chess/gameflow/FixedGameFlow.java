package chess.gameflow;

import chess.domain.gameflow.GameFlow;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceTeam;
import chess.domain.gameflow.state.State;
import chess.domain.gameflow.state.WhiteTeam;

public class FixedGameFlow implements GameFlow {

    private final State currentState = new WhiteTeam();

    public boolean isCorrectTurn(Piece sourcePiece) {
        return true;
    }

    @Override
    public void nextState(boolean isFinished) {
    }

    @Override
    public boolean isRunning() {
        return true;
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

