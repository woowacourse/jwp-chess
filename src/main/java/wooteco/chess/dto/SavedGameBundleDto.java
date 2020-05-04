package wooteco.chess.dto;

import java.util.List;

public class SavedGameBundleDto {
	private List<SavedGameDto> room;

	public SavedGameBundleDto(List<SavedGameDto> room) {
		this.room = room;
	}

	public List<SavedGameDto> getRoom() {
		return room;
	}
}
