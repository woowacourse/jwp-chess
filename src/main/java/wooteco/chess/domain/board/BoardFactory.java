package wooteco.chess.domain.board;

import static wooteco.chess.domain.piece.PawnMovingStrategy.*;
import static wooteco.chess.domain.position.Position.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
import wooteco.chess.domain.position.Row;

public class BoardFactory {
	private BoardFactory() {
	}

	public static Board of(String serializedBoard) {
		Map<Position, Piece> board = new HashMap<>();
		List<String> pieces = new ArrayList<>(Arrays.asList(serializedBoard.split("")));
		for (int rank = MAX_POSITION_INDEX; rank >= MIN_POSITION_INDEX; rank--) {
			board.putAll(putPerRank(pieces, rank));
		}
		return new Board(board);
	}

	private static Map<Position, Piece> putPerRank(List<String> pieces, int rank) {
		Map<Position, Piece> piecesInRank = new HashMap<>();

		for (Row row : Row.rowNames()) {
			String piece = pieces.remove(0);
			if (!piece.equals(".")) {
				piecesInRank.put(Position.of(row.getValue(), rank), PieceFactory.of(piece));
			}
		}
		return piecesInRank;
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
