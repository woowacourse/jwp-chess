package wooteco.chess.domain.piece;

import wooteco.chess.domain.Color;
import wooteco.chess.domain.PieceScore;
import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.piece.strategy.MoveStrategy;

import java.util.Map;

/**
 * 체스 기물을 의미하는 클래스입니다.
 *
 * @author AnHyungJu, LeeHoBin
 */
public abstract class Piece {
    protected Color color;
    protected MoveStrategy moveStrategy;
    private final String symbol;

    public Piece(Color color, String symbol, MoveStrategy moveStrategy) {
        this.color = color;
        this.symbol = symbol;
        this.moveStrategy = moveStrategy;
    }

    public String getSymbol() {
        return symbol;
    }

    public boolean isSameColor(Color color) {
        return this.color == color;
    }

    public abstract boolean isSameName(PieceScore pieceScore);

    public boolean isPawn() {
        return false;
    }

    public boolean isKing() {
        return false;
    }

    public boolean isBlank() {
        return false;
    }

    public abstract Path findPathByRule(Path path,
                                        Map<Position, Piece> pieces);

    public Color getColor() {
        return color;
    }
}
