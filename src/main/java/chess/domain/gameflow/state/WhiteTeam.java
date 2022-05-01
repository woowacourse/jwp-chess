package chess.domain.gameflow.state;

import chess.domain.piece.Piece;
import chess.domain.piece.PieceTeam;

public class WhiteTeam extends Running {

    private static final PieceTeam pieceTeam = PieceTeam.WHITE;

    private static final WhiteTeam CACHE = new WhiteTeam();
    public static final String NAME = "White Team";

    private WhiteTeam() {
    }

    public static WhiteTeam getInstance() {
        return CACHE;
    }

    @Override
    public boolean isSameColor(Piece sourcePiece) {
        return sourcePiece.isSameColor(pieceTeam);
    }

    @Override
    public State nextState(boolean isGameFinished) {
        if (isGameFinished) {
            return new Finish();
        }
        return BlackTeam.getInstance();
    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public PieceTeam pieceTeam() {
        return pieceTeam;
    }
}
