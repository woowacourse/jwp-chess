package wooteco.chess.dto;

import java.util.Map;

public class MoveResponseDto {
	private Long gameId;
	private boolean kingAlive;
	private String piece;
	private String turn;
	private Map<String, Double> score;

	protected MoveResponseDto() {
	}

	public MoveResponseDto(Long gameId, boolean kingAlive, String piece, String turn, Map<String, Double> score) {
		this.gameId = gameId;
		this.kingAlive = kingAlive;
		this.piece = piece;
		this.turn = turn;
		this.score =score;
	}

	public Long getGameId() {
		return gameId;
	}

	public boolean isKingAlive() {
		return kingAlive;
	}

	public String getPiece() {
		return piece;
	}

	public String getTurn() {
		return turn;
	}

	public Map<String, Double> getScore() {
		return score;
	}
}
