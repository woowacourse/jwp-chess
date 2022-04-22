package chess.controller.dto;

import chess.domain.Color;

public class ChessGameWinnerResponse {

	private String winner;

	private ChessGameWinnerResponse() {
	}

	private ChessGameWinnerResponse(String winner) {
		this.winner = winner;
	}

	public static ChessGameWinnerResponse from(Color color) {
		return new ChessGameWinnerResponse(color.name());
	}

	public String getWinner() {
		return winner;
	}
}
