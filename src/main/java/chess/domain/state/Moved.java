package chess.domain.state;

import chess.domain.pieces.Pieces;

public class Moved extends Playing {
    public Moved(Pieces pieces) {
        super(StateType.MOVED, pieces);
    }
}
