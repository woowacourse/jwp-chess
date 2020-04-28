package wooteco.chess.entity;

import org.springframework.data.relational.core.mapping.Table;

/**
 *    PieceEntity class입니다.
 *
 *    @author HyungJu An, JunSeong Hong
 */
@Table("piece")
public class PieceEntity {
	private final String position;
	private final String name;

	PieceEntity(final String position, final String name) {
		this.position = position;
		this.name = name;
	}

	public static PieceEntity of(final String position, final String name) {
		return new PieceEntity(position, name);
	}
}
