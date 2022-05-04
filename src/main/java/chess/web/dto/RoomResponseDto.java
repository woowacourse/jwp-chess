package chess.web.dto;

public class RoomResponseDto {

	private final int id;
	private final String name;
	private final boolean end;

	public RoomResponseDto(int id, String name, boolean end) {
		this.id = id;
		this.name = name;
		this.end = end;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public boolean getEnd() {
		return end;
	}
}
