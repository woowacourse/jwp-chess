package wooteco.chess.dto;

public class RoomDto {
	private final int id;
	private final String name;
	private final String player1Name;
	private final String player2Name;

	public RoomDto(int id, String player1Name, String player2Name) {
		this.id = id;
		this.name = "체스 " + id + "번방";
		this.player1Name = player1Name;
		this.player2Name = player2Name;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getPlayer1Name() {
		return player1Name;
	}

	public String getPlayer2Name() {
		return player2Name;
	}
}
