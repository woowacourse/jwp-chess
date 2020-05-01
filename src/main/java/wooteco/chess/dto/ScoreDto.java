package wooteco.chess.dto;

public class ScoreDto {
	private Double score;

	public ScoreDto(Double score) {
		this.score = score;
	}

	public double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}
}
