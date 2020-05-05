package wooteco.chess.domain.piece;

import java.util.Map;

import wooteco.chess.domain.Color;
import wooteco.chess.domain.PieceScore;
import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.piece.strategy.BlankMoveStrategy;

public class Blank extends Piece {
    private static final Blank blank = new Blank(Color.NONE, "blank");

    public Blank(Color color, String symbol) {
        super(color, symbol, new BlankMoveStrategy());
    }

    public static Blank getInstance() {
        return blank;
    }

    @Override
    public boolean isBlank() {
        return true;
    }

    @Override
    public Path findPathByRule(Path path, Map<Position, Piece> pieces) {
        return moveStrategy.findMovablePositions(path, pieces);
    }

    @Override
    public boolean isSameName(PieceScore pieceScore) {
        return false;
    }
}
