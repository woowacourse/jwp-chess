package wooteco.chess.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("turn")
public class TurnEntity {
	@Id
	private final Long id;

	private final Long roomId;
	private final boolean isWhiteTurn;

	TurnEntity(final Long id, final Long roomId, final boolean isWhiteTurn) {
		this.id = id;
		this.roomId = roomId;
		this.isWhiteTurn = isWhiteTurn;
	}

	public static TurnEntity of(final Long roomId, final boolean isWhiteTurn) {
		return new TurnEntity(null, roomId, isWhiteTurn);
	}

	public TurnEntity withId(final Long id) {
		return new TurnEntity(id, this.roomId, this.isWhiteTurn);
	}
}
