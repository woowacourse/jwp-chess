package wooteco.chess.dto;

public class PieceDto {
	private Long id;
	private Long gameId;
	private final String symbol;
	private final String team;
	private String position;

	public PieceDto(Long gameId, String symbol, String team, String position) {
		this.gameId = gameId;
		this.symbol = symbol;
		this.team = team;
		this.position = position;
	}

	public PieceDto(Long id, Long gameId, String symbol, String team, String position) {
		this(gameId, symbol, team, position);
		this.id = id;
	}

	public String getSymbol() {
		return symbol;
	}

	public String getTeam() {
		return team;
	}

	public String getPosition() {
		return position;
	}

	public long getGameId() {
		return this.gameId;
	}

	public Long getId() {
		return this.id;
	}
}
