package wooteco.chess.domain.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.Symbol;

@Table("piece")
public class PieceEntity {
	@Id
	private Long id;

	@Column("room_id")
	private String roomId;

	private String position;

	private String symbol;

	private PieceEntity(String roomId, String position, String symbol) {
		this.roomId = roomId;
		this.position = position;
		this.symbol = symbol;
	}

	public static PieceEntity of(String roomId, Piece piece) {
		return new PieceEntity(roomId, piece.getPosition().getName(), piece.getSymbol());
	}

	public String getPosition() {
		return position;
	}

	public String getSymbol() {
		return symbol;
	}

	public void updateSymbolToEmpty() {
		symbol = Symbol.EMPTY.getEmptySymbol();
	}

	public void updateSymbolToSource(PieceEntity source) {
		symbol = source.symbol;
	}
}
