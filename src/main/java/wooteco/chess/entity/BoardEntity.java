package wooteco.chess.entity;

import java.util.List;

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

	private final List<PieceEntity> pieces;
	private final TurnEntity turn;

	BoardEntity(final Long id, final List<PieceEntity> pieces, final TurnEntity turn) {
		this.id = id;
		this.pieces = pieces;
		this.turn = turn;
	}

	public static BoardEntity of(final List<PieceEntity> pieces, final TurnEntity turn) {
		return new BoardEntity(null, pieces, turn);
	}
}
