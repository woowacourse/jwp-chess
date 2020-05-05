package chess.dto;

public class RoomDto {
	private final String roomName;

	public RoomDto(final String roomName) {
		this.roomName = roomName;
	}

	public String getRoomName() {
		return roomName;
	}
}