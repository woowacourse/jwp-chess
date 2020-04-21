package wooteco.chess.domain.board;

import static wooteco.chess.domain.piece.PawnMovingStrategy.*;

import java.util.HashMap;
import java.util.Map;

import wooteco.chess.domain.piece.Bishop;
import wooteco.chess.domain.piece.Color;
import wooteco.chess.domain.piece.King;
import wooteco.chess.domain.piece.Knight;
import wooteco.chess.domain.piece.Pawn;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.Queen;
import wooteco.chess.domain.piece.Rook;
import wooteco.chess.domain.position.Column;
import wooteco.chess.domain.position.Position;

public class BoardFactory {
	private BoardFactory() {
	}

	public static Board create() {
		Map<Position, Piece> pieces = new HashMap<>();
		for (Color color : Color.values()) {
			initializeByColor(pieces, color);
		}
		initializePawn(pieces);
		return new Board(pieces);
	}

	private static void initializeByColor(Map<Position, Piece> pieces, Color color) {
		pieces.put(Position.of("A" + color.getInitialRow()), new Rook(color));
		pieces.put(Position.of("B" + color.getInitialRow()), new Knight(color));
		pieces.put(Position.of("C" + color.getInitialRow()), new Bishop(color));
		pieces.put(Position.of("D" + color.getInitialRow()), new Queen(color));
		pieces.put(Position.of("E" + color.getInitialRow()), new King(color));
		pieces.put(Position.of("F" + color.getInitialRow()), new Bishop(color));
		pieces.put(Position.of("G" + color.getInitialRow()), new Knight(color));
		pieces.put(Position.of("H" + color.getInitialRow()), new Rook(color));
	}

	public static Map<Position, Piece> initializePawn(Map<Position, Piece> pieces) {
		Column.columnNames()
			.stream()
			.map(Column::getName)
			.forEach(x -> {
				pieces.put(Position.of(x + WHITE_PAWN_ROW),
					new Pawn(Color.WHITE));
				pieces.put(Position.of(x + BLACK_PAWN_ROW),
					new Pawn(Color.BLACK));
			});

		return pieces;
	}
}
