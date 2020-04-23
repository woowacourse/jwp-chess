package wooteco.chess.dto;

public class MoveResponseDto {
	private Long gameId;
	private boolean kingAlive;
	private String piece;
	private String turn;

	public MoveResponseDto(Long gameId,boolean kingAlive, String piece, String turn) {
		this.gameId = gameId;
		this.kingAlive = kingAlive;
		this.piece = piece;
		this.turn = turn;
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
}
