package wooteco.chess.dto;

import wooteco.chess.domain.game.Score;
import wooteco.chess.domain.game.Status;
import wooteco.chess.domain.piece.Color;

public class StatusDto {
	private Double white;
	private Double black;
	private String winner;

	private StatusDto(Score white, Score black, Color winner) {
		this.white = white.getValue();
		this.black = black.getValue();
		this.winner = winner.name();
	}

	public static StatusDto from(Status status) {
		return new StatusDto(status.getWhiteScore(), status.getBlackScore(), status.getWinner());
	}

	public Double getWhite() {
		return white;
	}

	public void setWhite(Double white) {
		this.white = white;
	}

	public Double getBlack() {
		return black;
	}

	public void setBlack(Double black) {
		this.black = black;
	}

	public String getWinner() {
		return winner;
	}

	public void setWinner(String winner) {
		this.winner = winner;
	}
}
