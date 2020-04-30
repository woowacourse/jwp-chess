package wooteco.chess.domain.piece;

import java.util.Arrays;
import java.util.function.Function;

import wooteco.chess.domain.entity.PieceEntity;
import wooteco.chess.domain.position.Position;

public enum PieceFactory {
	WHITE_KING("♔", (position) -> new King(position, Turn.WHITE)),
	BLACK_KING("♚", (position) -> new King(position, Turn.BLACK)),
	WHITE_QUEEN("♕", (position) -> new Queen(position, Turn.WHITE)),
	BLACK_QUEEN("♛", (position) -> new Queen(position, Turn.BLACK)),
	WHITE_ROOK("♖", (position) -> new Rook(position, Turn.WHITE)),
	BLACK_ROOK("♜", (position) -> new Rook(position, Turn.BLACK)),
	WHITE_BISHOP("♗", (position) -> new Bishop(position, Turn.WHITE)),
	BLACK_BISHOP("♝", (position) -> new Bishop(position, Turn.BLACK)),
	WHITE_KNIGHT("♘", (position) -> new Knight(position, Turn.WHITE)),
	BLACK_KNIGHT("♞", (position) -> new Knight(position, Turn.BLACK)),
	WHITE_PAWN("♙", (position) -> new Pawn(position, Turn.WHITE)),
	BLACK_PAWN("♟", (position) -> new Pawn(position, Turn.BLACK)),
	EMPTY(" ", Empty::new);

	private final String symbol;
	private final Function<Position, Piece> pieceGenerator;

	PieceFactory(String symbol, Function<Position, Piece> pieceGenerator) {
		this.symbol = symbol;
		this.pieceGenerator = pieceGenerator;
	}

	public static PieceFactory of(String symbol) {
		return Arrays.stream(PieceFactory.values())
			.filter(value -> value.symbol.equals(symbol))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("존재 하지 않는 기물 입니다."));
	}


	public static Piece createBy(PieceEntity pieceEntity) {
		return Arrays.stream(PieceFactory.values())
			.filter(value -> value.symbol.equals(pieceEntity.getSymbol()))
			.map(value -> value.pieceGenerator.apply(pieceEntity.getPosition()))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("존재 하지 않는 기물 입니다."));
	}

	public Piece create(Position position) {
		return this.pieceGenerator.apply(position);
	}
}
