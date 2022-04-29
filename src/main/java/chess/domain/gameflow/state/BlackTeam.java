package chess.domain.gameflow.state;

import chess.domain.piece.Piece;
import chess.domain.piece.PieceTeam;

public class BlackTeam extends Running {

    private static final PieceTeam pieceTeam = PieceTeam.BLACK;

    private static final BlackTeam CACHE = new BlackTeam();

    public static final String name = "Black Team";

    private BlackTeam() {
    }

    public static BlackTeam getInstance() {
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
        return WhiteTeam.getInstance();
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public PieceTeam pieceTeam() {
        return pieceTeam;
    }
}
