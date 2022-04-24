package chess.turndecider.state;

import chess.domain.piece.Piece;
import chess.domain.piece.PieceTeam;

public class BlackTeam extends Running {

    private static final PieceTeam pieceTeam = PieceTeam.BLACK;
    private static final String name = "Black Team";

    @Override
    public boolean isSameColor(Piece sourcePiece) {
        return sourcePiece.isSameColor(pieceTeam);
    }

    @Override
    public State nextState(boolean isGameFinished) {
        if (isGameFinished) {
            return new Finish();
        }
        return new WhiteTeam();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public PieceTeam pieceTeam() {
        return pieceTeam;
    }
}
