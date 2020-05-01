package wooteco.chess.domain.piece;

import java.util.ArrayList;
import java.util.List;

import wooteco.chess.domain.position.Column;
import wooteco.chess.domain.position.Position;
import wooteco.chess.domain.position.Row;

public class PiecesFactory {
	private static final int BLACK_PAWN_ROW = 7;
	private static final int WHITE_PAWN_ROW = 2;
	private static final int FIRST_EMPTY_ROW = 3;
	private static final int LAST_EMPTY_ROW = 6;
	private static final char FIRST_COLUMN = 'a';
	private static final char LAST_COLUMN = 'h';
	private static final List<Piece> pieces = new ArrayList<>();

	static {
		initialize(Row.EIGHT, Turn.BLACK);
		initialize(Row.ONE, Turn.WHITE);

		for (char column = FIRST_COLUMN; column <= LAST_COLUMN; column++) {
			String blackPosition = String.valueOf(column) + BLACK_PAWN_ROW;
			pieces.add(new Pawn(Position.of(blackPosition), Turn.BLACK));

			String whitePosition = String.valueOf(column) + WHITE_PAWN_ROW;
			pieces.add(new Pawn(Position.of(whitePosition), Turn.WHITE));

			putEmpty(column);
		}
	}

	private static void initialize(Row row, Turn turn){
		pieces.add(new Rook(Position.of(Column.A, row), turn));
		pieces.add(new Knight(Position.of(Column.B, row), turn));
		pieces.add(new Bishop(Position.of(Column.C, row), turn));
		pieces.add(new Queen(Position.of(Column.D, row), turn));
		pieces.add(new King(Position.of(Column.E, row), turn));
		pieces.add(new Bishop(Position.of(Column.F, row), turn));
		pieces.add(new Knight(Position.of(Column.G, row), turn));
		pieces.add(new Rook(Position.of(Column.H, row), turn));
	}

	private static void putEmpty(char column) {
		for (int row = FIRST_EMPTY_ROW; row <= LAST_EMPTY_ROW; row++) {
			String position = String.valueOf(column) + row;
			pieces.add(new Empty(Position.of(position)));
		}
	}

	public static List<Piece> createInitial() {
		return List.copyOf(pieces);
	}
}
