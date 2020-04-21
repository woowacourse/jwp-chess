package wooteco.chess.domain.piece;

import wooteco.chess.domain.Color;
import wooteco.chess.domain.PieceScore;
import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.piece.strategy.BasicRepeatMoveStrategy;

import java.util.Map;

public class Queen extends Piece {
	public Queen(Color color, String symbol) {
		super(color, symbol, new BasicRepeatMoveStrategy(Direction.everyDirection()));
	}

	@Override
	public boolean isSameName(PieceScore pieceScore) {
		return PieceScore.QUEEN == pieceScore;
	}

	@Override
	public Path findPathByRule(Path path, Map<Position, Piece> pieces) {
		return moveStrategy.findMovablePositions(path, pieces);
	}
}
