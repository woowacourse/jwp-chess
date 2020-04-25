package wooteco.chess.dto;

public class GameDto {
	private Long id;
	private String turn;

	public GameDto(Long id, String turn) {
		this.id = id;
		this.turn = turn;
	}

	public GameDto(String turn) {
		this.turn = turn;
	}

	public String getTurn() {
		return turn;
	}

	public Long getId() {
		return id;
	}
}
