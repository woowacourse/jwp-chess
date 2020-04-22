package chess.domain;

public class GameResult {
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
			return "White";
		}
		if (isWhiteKingKilled) {
			return "Black";
		}
		return "";
	}

	private String findLoser(boolean isBlackKingKilled, boolean isWhiteKingKilled) {
		if (isBlackKingKilled) {
			return "Black";
		}
		if (isWhiteKingKilled) {
			return "White";
		}
		return "";
	}

	public String findWinner() {
		return winner;
	}

	public String findLoser() {
		return loser;
	}

	public double getAliveBlackPieceScoreSum() {
		return aliveBlackPieceScoreSum;
	}

	public double getAliveWhitePieceScoreSum() {
		return aliveWhitePieceScoreSum;
	}
}
