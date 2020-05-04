package wooteco.chess.dto;

public class RoomDto {
	private final long id;
	private final String title;

	public RoomDto(long id, String title) {
		this.id = id;
		this.title = title;
	}

	public long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}
}
