package chess.gameflow.state;

import chess.domain.piece.Piece;
import chess.domain.piece.PieceTeam;

public class Finish implements State {

    public static final String UNSUPPORTED_FUNCITON_MESSAGE = "[ERROR] 지원하지 않는 기능입니다.";

    @Override
    public boolean isSameColor(Piece sourcePiece) {
        return false;
    }

    @Override
    public State nextState(boolean isGameFinished) {
        throw new UnsupportedOperationException(UNSUPPORTED_FUNCITON_MESSAGE);
    }

    @Override
    public boolean isRunning() {
        return false;
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public PieceTeam pieceTeam() {
        throw new UnsupportedOperationException(UNSUPPORTED_FUNCITON_MESSAGE);
    }
}
