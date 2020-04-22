package wooteco.chess.domain.judge;

import wooteco.chess.domain.piece.Side;

public interface Judge {
    double calculateScore(final Side side);

    boolean isGameOver();

    boolean isDraw();

    Side winner();
}
