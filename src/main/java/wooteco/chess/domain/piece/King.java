package wooteco.chess.domain.piece;

import wooteco.chess.domain.Color;
import wooteco.chess.domain.PieceScore;
import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.piece.strategy.BasicOneMoveStrategy;

import java.util.Map;

public class King extends Piece {
	public King(Color color, String symbol) {
		super(color, symbol, new BasicOneMoveStrategy(Direction.everyDirection()));
	}

	@Override
	public boolean isSameName(PieceScore pieceScore) {
		return PieceScore.KING == pieceScore;
	}

	@Override
	public Path findPathByRule(Path path, Map<Position, Piece> pieces) {
		return moveStrategy.findMovablePositions(path, pieces);
	}

	@Override
	public boolean isKing() {
		return true;
	}
}
