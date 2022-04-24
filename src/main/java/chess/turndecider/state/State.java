package chess.turndecider.state;

import chess.domain.piece.Piece;
import chess.domain.piece.PieceTeam;

public interface State {

    boolean isSameColor(Piece sourcePiece);

    State nextState(boolean isGameFinished);

    boolean isRunning();

    String getName();

    PieceTeam pieceTeam();
}
