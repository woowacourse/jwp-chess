package chess.web.dto;

public class RoomResponseDto {

	private final long id;
	private final String name;
	private final boolean end;

	public RoomResponseDto(long id, String name, boolean end) {
		this.id = id;
		this.name = name;
		this.end = end;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public boolean getEnd() {
		return end;
	}
}
