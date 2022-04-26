package chess.gameflow.state;

import chess.domain.piece.Piece;
import chess.domain.piece.PieceTeam;

public interface State {

    boolean isSameColor(Piece sourcePiece);

    State nextState(boolean isGameFinished);

    boolean isRunning();

    String name();

    PieceTeam pieceTeam();
}
