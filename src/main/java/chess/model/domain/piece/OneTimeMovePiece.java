package chess.model.domain.piece;

import chess.model.domain.board.Square;

public abstract class OneTimeMovePiece extends Piece {

    protected OneTimeMovePiece(Team team, Type type) {
        super(team, type);
    }

    @Override
    protected int getRepeatCount() {
        return Square.MIN_FILE_AND_RANK_COUNT;
    }

}
