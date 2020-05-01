package wooteco.chess.dto;

import wooteco.chess.domain.game.Turn;

public class TurnDto {
	private String turn;

	private TurnDto(String turn) {
		this.turn = turn;
	}

	public static TurnDto from(Turn turn) {
		return new TurnDto(String.valueOf(turn));
	}

	public String getTurn() {
		return turn;
	}

	public void setTurn(String turn) {
		this.turn = turn;
	}
}
