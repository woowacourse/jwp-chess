package wooteco.chess.dto;

public class MoveRequestDto {
	private Long gameId;
	private String command;

	public MoveRequestDto(Long gameId, String command) {
		this.gameId = gameId;
		this.command = command;
	}

	public Long getGameId() {
		return gameId;
	}

	public String getCommand() {
		return command;
	}
}
