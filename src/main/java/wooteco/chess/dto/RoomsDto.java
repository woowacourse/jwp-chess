package wooteco.chess.dto;

import java.util.List;

public class RoomsDto {
	private List<RoomDto> room;

	public RoomsDto(List<RoomDto> room) {
		this.room = room;
	}

	public List<RoomDto> getRoom() {
		return room;
	}
}
