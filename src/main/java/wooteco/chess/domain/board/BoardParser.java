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
	private static final String EMPTY_ZONE = ".";

	public static Map<Position, Piece> parsePieces(String board) {
		Map<Position, Piece> pieces = new HashMap<>();
		String[] rowBoards = board.split(ROW_SEPARATOR);
		for (int rowIndex = MIN_POSITION_NUMBER; rowIndex <= MAX_POSITION_NUMBER; rowIndex++) {
			Map<Position, Piece> rowPieces = parseRow(rowBoards[rowIndex - 1], rowIndex);
			pieces.putAll(rowPieces);
		}
		return pieces;
	}

	private static Map<Position, Piece> parseRow(String rowBoard, int index) {
		Map<Position, Piece> rowPieces = new HashMap<>();
		String[] rowElements = rowBoard.split(COLUMN_SEPARATOR);
		for (int col = 1; col <= MAX_POSITION_NUMBER; col++) {
			parseOneElement(Position.of(col, parseRank(index)), rowElements[col - 1], rowPieces);
		}
		return rowPieces;
	}

	private static int parseRank(int rowIndex) {
		return MAX_POSITION_NUMBER + 1 - rowIndex;
	}

	private static void parseOneElement(Position position, String positionElement, Map<Position, Piece> row) {
		if (EMPTY_ZONE.equals(positionElement)) {
			return;
		}
		row.put(position, PieceFactory.of(positionElement));
	}

	public static String parseString(Board board) {
		StringBuilder builder = new StringBuilder();
		for (int row = MAX_POSITION_NUMBER; row >= MIN_POSITION_NUMBER; row--) {
			builder.append(parseRowString(board, row));
		}
		return builder.toString();
	}

	private static String parseRowString(Board board, int row) {
		StringBuilder builder = new StringBuilder();
		for (int col = MIN_POSITION_NUMBER; col <= MAX_POSITION_NUMBER; col++) {
			Piece piece = board.findPiece(Position.of(col, row));
			builder.append(piece.getSymbol());
		}
		builder.append(ROW_SEPARATOR);
		return builder.toString();
	}
}
