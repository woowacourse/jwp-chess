package wooteco.chess.dto;

public class RoomDto {
	private String name;

	private RoomDto(String name) {
		this.name = name;
	}

	public static RoomDto of(String name) {
		return new RoomDto(name);
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}
}
