package wooteco.chess.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

/**
 *    Room entity class입니다.
 *
 *    @author HyungJu An, JunSeong Hong
 */
@Table("room")
public class RoomEntity {
	@Id
	private final Long id;

	private final BoardEntity boards;

	RoomEntity(final Long id, final BoardEntity boards) {
		this.id = id;
		this.boards = boards;
	}

	public static RoomEntity of(final BoardEntity boards) {
		return new RoomEntity(null, boards);
	}
}
