package wooteco.chess.dto;

public class PlayerDto {
	private final String name;
	private final String password;
	private final String team;

	public PlayerDto(String name, String password, String team) {
		this.name = name;
		this.password = password;
		this.team = team;
	}

	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}

	public String getTeam() {
		return team;
	}
}
