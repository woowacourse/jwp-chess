package wooteco.chess.domain.piece;

import java.util.Map;

import wooteco.chess.domain.Color;
import wooteco.chess.domain.PieceScore;
import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.piece.strategy.PawnMoveStrategy;

public class Pawn extends Piece {
    public Pawn(Color color, String symbol) {
        super(color, symbol, new PawnMoveStrategy(color));
    }

    @Override
    public boolean isSameName(PieceScore pieceScore) {
        return PieceScore.PAWN == pieceScore;
    }

    @Override
    public boolean isPawn() {
        return true;
    }

    @Override
    public Path findPathByRule(Path path, Map<Position, Piece> pieces) {
        return moveStrategy.findMovablePositions(path, pieces);
    }
}
