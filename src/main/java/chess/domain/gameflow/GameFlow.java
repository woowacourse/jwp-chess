package chess.domain.gameflow;

import chess.domain.piece.Piece;
import chess.domain.piece.PieceTeam;

public interface GameFlow {

    boolean isCorrectTurn(Piece sourcePiece);

    void nextState(boolean isGameFinished);

    boolean isRunning();

    String currentStateName();
    PieceTeam currentPieceTeam();
}
