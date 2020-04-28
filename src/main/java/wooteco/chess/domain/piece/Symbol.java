package wooteco.chess.domain.piece;

import java.util.List;

public enum Symbol {
	KING("♚", "♔"),
	QUEEN("♛", "♕"),
	ROOK("♜", "♖"),
	BISHOP("♝", "♗"),
	KNIGHT("♞", "♘"),
	PAWN("♟", "♙"),
	EMPTY(" ", " ");

	private final String blackSymbol;
	private final String whiteSymbol;

	Symbol(String blackSymbol, String whiteSymbol) {
		this.blackSymbol = blackSymbol;
		this.whiteSymbol = whiteSymbol;
	}

	public String getBlackSymbol() {
		return blackSymbol;
	}

	public String getWhiteSymbol() {
		return whiteSymbol;
	}

	public String getEmptySymbol() {
		return " ";
	}

	public List<String> symbols() {
		return List.of(blackSymbol, whiteSymbol);
	}
}