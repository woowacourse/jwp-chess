package wooteco.chess.dto;

import org.springframework.data.annotation.Id;

public class RoomDto {
	@Id
	private Long id;
	private int turnId;
	private int player1Id;
	private int player2Id;
	private String name;
	private String player1Name;
	private String player2Name;

	public RoomDto(Long id, String player1Name, String player2Name) {
		this.id = id;
		this.name = "체스 " + id + "번방";
		this.player1Name = player1Name;
		this.player2Name = player2Name;
	}

	public RoomDto() {
	}

	public Long getId() {
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

	public void setId(Long id) {
		this.id = id;
	}

	public int getTurnId() {
		return turnId;
	}

	public void setTurnId(int turnId) {
		this.turnId = turnId;
	}

	public int getPlayer1Id() {
		return player1Id;
	}

	public void setPlayer1Id(int player1Id) {
		this.player1Id = player1Id;
	}

	public int getPlayer2Id() {
		return player2Id;
	}

	public void setPlayer2Id(int player2Id) {
		this.player2Id = player2Id;
	}
}
