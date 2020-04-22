package wooteco.chess.domain.board;

import static wooteco.chess.domain.position.Position.*;

import java.util.HashMap;
import java.util.Map;

import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.PieceFactory;
import wooteco.chess.domain.position.Position;

public class BoardParser {
	private static final String ROW_SEPARATOR = "\n";
	private static final String COLUMN_SEPARATOR = "";

	public static Map<Position, Piece> parsePieces(String board) {
		String[] boardRow = board.split(ROW_SEPARATOR);
		Map<Position, Piece> pieces = new HashMap<>();
		for (int row = MINIMUM_POSITION_NUMBER; row <= MAXIMUM_POSITION_NUMBER; row++) {
			Map<Position, Piece> rowPieces = parseRow(boardRow[row - 1], row);
			pieces.putAll(rowPieces);
		}

		return pieces;
	}

	private static Map<Position, Piece> parseRow(String boardRow, int row) {
		Map<Position, Piece> rowPieces = new HashMap<>();
		String[] rowElements = boardRow.split(COLUMN_SEPARATOR);
		for (int col = 1; col <= MAXIMUM_POSITION_NUMBER; col++) {
			parseOneElement(Position.of(col, MAXIMUM_POSITION_NUMBER + 1 - row), rowElements[col - 1], rowPieces);
		}

		return rowPieces;
	}

	private static void parseOneElement(Position of, String rowElement, Map<Position, Piece> row) {
		if (".".equals(rowElement)) {
			return;
		}
		row.put(of, PieceFactory.of(rowElement));
	}

	public static String parseString(Board board) {
		StringBuilder builder = new StringBuilder();
		for (int row = MAXIMUM_POSITION_NUMBER; row >= MINIMUM_POSITION_NUMBER; row--) {
			builder.append(parseRowString(board, row));
		}
		return builder.toString();
	}

	private static String parseRowString(Board board, int row) {
		StringBuilder builder = new StringBuilder();
		for (int col = MINIMUM_POSITION_NUMBER; col <= MAXIMUM_POSITION_NUMBER; col++) {
			Piece piece = board.findPiece(Position.of(col, row));
			builder.append(piece.getSymbol());
		}
		builder.append(ROW_SEPARATOR);
		return builder.toString();
	}
}
