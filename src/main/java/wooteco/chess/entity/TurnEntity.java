package wooteco.chess.entity;

import org.springframework.data.relational.core.mapping.Table;

@Table("turn")
public class TurnEntity {
	private final boolean isWhiteTurn;

	TurnEntity(final boolean isWhiteTurn) {
		this.isWhiteTurn = isWhiteTurn;
	}

	public static TurnEntity of(final boolean isWhiteTurn) {
		return new TurnEntity(isWhiteTurn);
	}
}
