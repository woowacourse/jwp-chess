package chess.domain.state;

import chess.domain.pieces.Pieces;

public class Reported extends NotPlaying {
    public Reported(Pieces pieces) {
        super(StateType.REPORTED, pieces);
    }
}
