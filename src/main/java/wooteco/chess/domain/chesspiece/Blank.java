package wooteco.chess.domain.chesspiece;

import wooteco.chess.domain.position.Position;
import wooteco.chess.domain.position.Positions;

public class Blank extends Piece {
	private static final String NAME = ".";
	private static final String NOT_SUPPORT_MESSAGE = "BLANK에서는 지원하지 않는 기능입니다.";

	public Blank(Position position) {
		super(position, null);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public boolean isNotNeedCheckPath() {
		throw new UnsupportedOperationException(NOT_SUPPORT_MESSAGE);
	}

	@Override
	public Positions makePathAndValidate(Piece targetPiece) {
		throw new UnsupportedOperationException(NOT_SUPPORT_MESSAGE);
	}

	@Override
	public void validateCanGo(Piece targetPiece) {
		throw new UnsupportedOperationException(NOT_SUPPORT_MESSAGE);
	}
}