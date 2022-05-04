package chess.web.dto;

public class GameStateDto {
	private final String end;
	private final String turn;

	public GameStateDto(String end, String turn) {
		this.end = end;
		this.turn = turn;
	}

	public String getEnd() {
		return end;
	}

	public String getTurn() {
		return turn;
	}
}
