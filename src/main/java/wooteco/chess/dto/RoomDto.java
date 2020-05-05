package wooteco.chess.dto;

public class RoomDto {
	private final Long id;
	private final String title;

	public RoomDto(Long id, String title) {
		this.id = id;
		this.title = title;
	}

	public long getId() {
		return id;
	}

}
