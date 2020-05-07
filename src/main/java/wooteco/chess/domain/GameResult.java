package wooteco.chess.domain;

import wooteco.chess.domain.piece.PieceColor;

public class GameResult {
	private static final String EMPTY_RESULT = "";
	private final String winner;
	private final String loser;
	private final double aliveBlackPieceScoreSum;
	private final double aliveWhitePieceScoreSum;

	public GameResult(boolean isBlackKingKilled, boolean isWhiteKingKilled, double aliveBlackPieceScoreSum,
		double aliveWhitePieceScoreSum) {
		this.winner = findWinner(isBlackKingKilled, isWhiteKingKilled);
		this.loser = findLoser(isBlackKingKilled, isWhiteKingKilled);
		this.aliveBlackPieceScoreSum = aliveBlackPieceScoreSum;
		this.aliveWhitePieceScoreSum = aliveWhitePieceScoreSum;
	}

	private String findWinner(boolean isBlackKingKilled, boolean isWhiteKingKilled) {
		if (isBlackKingKilled) {
			return PieceColor.WHITE.getName();
		}
		if (isWhiteKingKilled) {
			return PieceColor.BLACK.getName();
		}
		return EMPTY_RESULT;
	}

	private String findLoser(boolean isBlackKingKilled, boolean isWhiteKingKilled) {
		if (isBlackKingKilled) {
			return PieceColor.BLACK.getName();
		}
		if (isWhiteKingKilled) {
			return PieceColor.WHITE.getName();
		}
		return EMPTY_RESULT;
	}

	public String getWinner() {
		return winner;
	}

	public String getLoser() {
		return loser;
	}

	public double getAliveBlackPieceScoreSum() {
		return aliveBlackPieceScoreSum;
	}

	public double getAliveWhitePieceScoreSum() {
		return aliveWhitePieceScoreSum;
	}
}
