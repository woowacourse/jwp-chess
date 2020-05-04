package wooteco.chess.domain.factory;

public enum BoardIndex {
	BLACK_TEAM_EXECUTIVE(8),
	BLACK_TEAM_PAWN(7),
	BLACK_TO(6),
	BLACK_FROM(3),
	WHITE_TEAM_PAWN(2),
	WHITE_TEAM_EXECUTIVE(1),
	BOARD_FROM(1),
	BOARD_TO(8),
	ROOK_FIRST(1),
	KNIGHT_FIRST(2),
	BISHOP_FIRST(3),
	QUEEN(4),
	KING(5),
	BISHOP_SECOND(6),
	KNIGHT_SECOND(7),
	ROOK_SECOND(8),
	ROW_FROM('a'),
	ROW_TO('h');

	public final int index;

	BoardIndex(int index) {
		this.index = index;
	}
}
