package wooteco.chess.domain.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("PIECE")
public class GamePiece {
	@Id
	private Long id;
	private String symbol;
	private String position;
	private String team;

	protected GamePiece() {
	}

	public GamePiece(String symbol, String position, String team) {
		this.symbol = symbol;
		this.position = position;
		this.team = team;
	}

	public GamePiece(Long id, String symbol, String position, String team) {
		this(symbol, position, team);
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public String getSymbol() {
		return symbol;
	}

	public String getPosition() {
		return position;
	}

	public String getTeam() {
		return team;
	}
}
