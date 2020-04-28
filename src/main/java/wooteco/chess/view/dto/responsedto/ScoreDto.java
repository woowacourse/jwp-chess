package wooteco.chess.view.dto.responsedto;

import java.util.Objects;

public class ScoreDto {
	private final String team;
	private final double score;

	public ScoreDto(String team, double score) {
		this.team = team;
		this.score = score;
	}

	public String getTeam() {
		return team;
	}

	public double getScore() {
		return score;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ScoreDto scoreDTO = (ScoreDto)o;
		return Double.compare(scoreDTO.score, score) == 0 &&
			Objects.equals(team, scoreDTO.team);
	}

	@Override
	public int hashCode() {
		return Objects.hash(team, score);
	}

	@Override
	public String toString() {
		return "ScoreDTO{" +
			"team='" + team + '\'' +
			", score=" + score +
			'}';
	}
}
