package wooteco.chess.dto;

import wooteco.chess.domain.position.Position;

public class PieceDto {
	private final Position position;
	private final String team;
	private final String symbol;

	public PieceDto(Position position, String team, String symbol) {
		this.position = position;
		this.team = team;
		this.symbol = symbol;
	}

	public Position getPosition() {
		return position;
	}

	public String getTeam() {
		return team;
	}

	public String getSymbol() {
		return symbol;
	}
}
