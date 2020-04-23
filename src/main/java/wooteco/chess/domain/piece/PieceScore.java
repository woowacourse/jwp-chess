package wooteco.chess.domain.piece;

import java.util.Arrays;
import java.util.List;

public enum PieceScore {
	KING(Symbol.KING.symbols(), 0),
	QUEEN(Symbol.QUEEN.symbols(), 9),
	ROOK(Symbol.ROOK.symbols(), 5),
	BISHOP(Symbol.BISHOP.symbols(), 3),
	KNIGHT(Symbol.KNIGHT.symbols(), 2.5),
	PAWN(Symbol.PAWN.symbols(), 1);

	private static final int PAWN_PENALTY_BOUND = 2;
	private static final double PAWN_PENALTY = 0.5;

	private final List<String> symbols;
	private final double score;

	PieceScore(List<String> symbols, double score) {
		this.symbols = symbols;
		this.score = score;
	}

	public static double calculateScoreOf(List<Piece> column) {
		double score = column.stream()
			.mapToDouble(PieceScore::getScoreOf)
			.sum();
		return score - calculatePenalty(column);
	}

	private static double getScoreOf(Piece piece) {
		return Arrays.stream(values())
			.filter(value -> value.symbols.contains(piece.getSymbol()))
			.findFirst()
			.orElseThrow(AssertionError::new)
			.score;
	}

	private static double calculatePenalty(List<Piece> column) {
		if (countPawnIn(column) >= PAWN_PENALTY_BOUND) {
			return countPawnIn(column) * PAWN_PENALTY;
		}
		return 0;
	}

	private static long countPawnIn(List<Piece> column) {
		return column.stream()
			.filter(Piece::isPenaltyApplier)
			.count();
	}
}
