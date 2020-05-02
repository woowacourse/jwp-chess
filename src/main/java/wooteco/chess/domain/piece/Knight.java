package wooteco.chess.domain.piece;

import java.util.Map;

import wooteco.chess.domain.Color;
import wooteco.chess.domain.PieceScore;
import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.piece.strategy.BasicOneMoveStrategy;

public class Knight extends Piece {
    public Knight(Color color, String symbol) {
        super(color, symbol, new BasicOneMoveStrategy(Direction.knightDirection()));
    }

    @Override
    public boolean isSameName(PieceScore pieceScore) {
        return PieceScore.KNIGHT == pieceScore;
    }

    @Override
    public Path findPathByRule(Path path, Map<Position, Piece> pieces) {
        return moveStrategy.findMovablePositions(path, pieces);
    }
}
