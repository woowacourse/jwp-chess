package wooteco.chess.domain.piece;

import java.util.Map;

import wooteco.chess.domain.Color;
import wooteco.chess.domain.PieceScore;
import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.piece.strategy.BasicRepeatMoveStrategy;

public class Bishop extends Piece {
    public Bishop(Color color, String symbol) {
        super(color, symbol, new BasicRepeatMoveStrategy(Direction.diagonalDirection()));
    }

    @Override
    public boolean isSameName(PieceScore pieceScore) {
        return PieceScore.BISHOP == pieceScore;
    }

    @Override
    public Path findPathByRule(Path path, Map<Position, Piece> pieces) {
        return moveStrategy.findMovablePositions(path, pieces);
    }
}
