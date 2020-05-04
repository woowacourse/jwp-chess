package wooteco.chess.entity;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import wooteco.chess.domain.Turn;

@Table("turn")
public class TurnEntity {
	@Column("is_white_turn")
	private final Boolean isWhiteTurn;

	private TurnEntity(final boolean isWhiteTurn) {
		this.isWhiteTurn = isWhiteTurn;
	}

	public static TurnEntity of(final boolean isWhiteTurn) {
		return new TurnEntity(isWhiteTurn);
	}

	public Turn createTurn() {
		return new Turn(isWhiteTurn);
	}
}
