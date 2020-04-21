package wooteco.chess.domain.board;

import java.util.ArrayList;
import java.util.List;

import wooteco.chess.domain.piece.Bishop;
import wooteco.chess.domain.piece.Empty;
import wooteco.chess.domain.piece.King;
import wooteco.chess.domain.piece.Knight;
import wooteco.chess.domain.piece.Pawn;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.Queen;
import wooteco.chess.domain.piece.Rook;
import wooteco.chess.domain.piece.Team;
import wooteco.chess.domain.position.Column;
import wooteco.chess.domain.position.Position;
import wooteco.chess.domain.position.Row;

public class BoardFactory {
	private static final int BLACK_PAWN_ROW = 7;
	private static final int WHITE_PAWN_ROW = 2;
	private static final int FIRST_EMPTY_ROW = 3;
	private static final int LAST_EMPTY_ROW = 6;
	private static final char FIRST_COLUMN = 'a';
	private static final char LAST_COLUMN = 'h';
	private static final List<Piece> board = new ArrayList<>();

	static {
		initialize(Row.EIGHT, Team.BLACK);
		initialize(Row.ONE, Team.WHITE);

		for (char column = FIRST_COLUMN; column <= LAST_COLUMN; column++) {
			String blackPosition = String.valueOf(column) + BLACK_PAWN_ROW;
			board.add(new Pawn(Position.of(blackPosition), Team.BLACK));

			String whitePosition = String.valueOf(column) + WHITE_PAWN_ROW;
			board.add(new Pawn(Position.of(whitePosition), Team.WHITE));

			putEmpty(column);
		}
	}

	private static void initialize(Row row, Team team){
		board.add(new Rook(Position.of(Column.A, row), team));
		board.add(new Knight(Position.of(Column.B, row), team));
		board.add(new Bishop(Position.of(Column.C, row), team));
		board.add(new Queen(Position.of(Column.D, row), team));
		board.add(new King(Position.of(Column.E, row), team));
		board.add(new Bishop(Position.of(Column.F, row), team));
		board.add(new Knight(Position.of(Column.G, row), team));
		board.add(new Rook(Position.of(Column.H, row), team));
	}

	private static void putEmpty(char column) {
		for (int row = FIRST_EMPTY_ROW; row <= LAST_EMPTY_ROW; row++) {
			String position = String.valueOf(column) + row;
			board.add(new Empty(Position.of(position)));
		}
	}

	public static List<Piece> toList() {
		return List.copyOf(board);
	}
}
