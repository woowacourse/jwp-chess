package wooteco.chess.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

/**
 *    board entity class입니다.
 *
 *    @author HyungJu An, JunSeong Hong
 */
@Table("board")
public class BoardEntity {
	@Id
	private final Long id;

	private final Long roomId;
	private final String position;
	private final String name;

	BoardEntity(final Long id, final Long roomId, final String position, final String name) {
		this.id = id;
		this.roomId = roomId;
		this.position = position;
		this.name = name;
	}

	public static BoardEntity of(final Long roomId, final String position, final String name) {
		return new BoardEntity(null, roomId, position, name);
	}

	public BoardEntity withId(final Long id) {
		return new BoardEntity(id, this.roomId, this.position, this.name);
	}
}
