package wooteco.chess.dto;

import java.util.List;

public class SavedGameBundleDto {
	private List<SavedGameDto> rooms;

	public SavedGameBundleDto(List<SavedGameDto> rooms) {
		this.rooms = rooms;
	}

	public List<SavedGameDto> getRooms() {
		return rooms;
	}
}
