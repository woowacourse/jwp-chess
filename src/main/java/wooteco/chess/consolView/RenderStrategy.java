package wooteco.chess.consolView;

import wooteco.chess.domain.piece.Piece;

public interface RenderStrategy {
    String render(Piece piece);
}
