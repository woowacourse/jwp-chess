package wooteco.chess.domain.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.Symbol;
import wooteco.chess.domain.position.Position;

@Table("piece")
public class PieceEntity {
	@Id
	private Long id;

	private Position position;

	private String symbol;

	private PieceEntity(Position position, String symbol) {
		this.position = position;
		this.symbol = symbol;
	}

	public static PieceEntity of(Piece piece) {
		return new PieceEntity(piece.getPosition(), piece.getSymbol());
	}

	public Position getPosition() {
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

	public boolean isPositionEquals(Position position) {
		return this.position.equals(position);
	}
}
