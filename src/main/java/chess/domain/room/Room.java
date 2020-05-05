package chess.domain.room;

public class Room {
	private final String name;

	public Room(final String name) {
		validate(name);
		this.name = name;
	}

	private void validate(final String name) {
		if (name.length() > 20) {
			throw new IllegalArgumentException("방 이름이 너무 길어요.");
		}
	}

	public String getName() {
		return name;
	}
}
