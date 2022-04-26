package chess.web.dto;

public class RoomResponseDto {

	private final long id;
	private final String name;

	public RoomResponseDto(long id, String name) {
		this.id = id;
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
