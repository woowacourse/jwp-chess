package wooteco.chess.util;

import static wooteco.chess.domain.board.Board.MAX_COLUMN_COUNT;
import static wooteco.chess.domain.board.Board.MIN_COLUMN_COUNT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wooteco.chess.domain.board.Rank;
import wooteco.chess.domain.piece.Piece;

public class UnicodeConverter {
	private static Map<String, String> unicode = new HashMap<>();
	private static final String BLANK = "";

	static {
		unicode.put("R", "♜");
		unicode.put("N", "♞");
		unicode.put("B", "♝");
		unicode.put("Q", "♛");
		unicode.put("K", "♚");
		unicode.put("P", "♟");

		unicode.put("r", "♖");
		unicode.put("n", "♘");
		unicode.put("b", "♗");
		unicode.put("q", "♕");
		unicode.put("k", "♔");
		unicode.put("p", "♙");

		unicode.put("", "");
	}

	public static List<String> convert(List<Rank> board) {
		List<String> pieces = new ArrayList<>();
		for (Rank rank : board) {
			convertRank(pieces, rank);
		}
		return pieces;
	}

	private static void convertRank(List<String> pieces, Rank rank) {
		for (int i = MIN_COLUMN_COUNT; i <= MAX_COLUMN_COUNT; i++) {
			final int columnNumber = i;
			String pieceSymbol = rank.getPieces().stream()
				.filter(p -> p.equalsColumn(columnNumber))
				.map(Piece::getSymbol)
				.findFirst()
				.orElse(BLANK);
			pieces.add(map(pieceSymbol));
		}
	}

	private static String map(String symbol) {
		return unicode.get(symbol);
	}
}