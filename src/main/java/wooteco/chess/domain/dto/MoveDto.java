package wooteco.chess.domain.dto;

public class MoveDto {
	private String roomName;
	private String source;
	private String target;

	public MoveDto(String roomName, String source, String target) {
		this.roomName = roomName;
		this.source = source;
		this.target = target;
	}

	public String getRoomName() {
		return roomName;
	}

	public String getSource() {
		return source;
	}

	public String getTarget() {
		return target;
	}
}
