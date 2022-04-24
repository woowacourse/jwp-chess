package chess.turndecider.state;

import chess.domain.piece.Piece;
import chess.domain.piece.PieceTeam;

public class WhiteTeam extends Running {

    private static final PieceTeam pieceTeam = PieceTeam.WHITE;
    private static final String name = "흰색 팀";

    @Override
    public boolean isSameColor(Piece sourcePiece) {
        return sourcePiece.isSameColor(pieceTeam);
    }

    @Override
    public State nextState(boolean isGameFinished) {
        if (isGameFinished) {
            return new Finish();
        }
        return new BlackTeam();
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
