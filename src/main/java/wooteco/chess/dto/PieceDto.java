package wooteco.chess.dto;

public class PieceDto {
	private final int x;
	private final int y;
	private final String team;
	private final String unit;

	public PieceDto(int x, int y, String team, String unit) {
		this.x = x;
		this.y = y;
		this.team = team;
		this.unit = unit;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public String getTeam() {
		return team;
	}

	public String getUnit() {
		return unit;
	}
}
