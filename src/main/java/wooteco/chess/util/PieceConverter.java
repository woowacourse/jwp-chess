package wooteco.chess.util;

import java.util.Arrays;
import java.util.function.Function;

import wooteco.chess.domain.piece.Bishop;
import wooteco.chess.domain.piece.King;
import wooteco.chess.domain.piece.Knight;
import wooteco.chess.domain.piece.Pawn;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.Queen;
import wooteco.chess.domain.piece.Rook;
import wooteco.chess.domain.piece.position.Position;
import wooteco.chess.domain.piece.team.Team;

public enum PieceConverter {
	WHITE_PAWN("p", position -> new Pawn(Position.of(position), Team.WHITE)),
	WHITE_ROOK("r", position -> new Rook(Position.of(position), Team.WHITE)),
	WHITE_KNIGHT("n", position -> new Knight(Position.of(position), Team.WHITE)),
	WHITE_BISHOP("b", position -> new Bishop(Position.of(position), Team.WHITE)),
	WHITE_QUEEN("q", position -> new Queen(Position.of(position), Team.WHITE)),
	WHITE_KING("k", position -> new King(Position.of(position), Team.WHITE)),

	BLACK_PAWN("P", position -> new Pawn(Position.of(position), Team.BLACK)),
	BLACK_ROOK("R", position -> new Rook(Position.of(position), Team.BLACK)),
	BLACK_KNIGHT("N", position -> new Knight(Position.of(position), Team.BLACK)),
	BLACK_BISHOP("B", position -> new Bishop(Position.of(position), Team.BLACK)),
	BLACK_QUEEN("Q", position -> new Queen(Position.of(position), Team.BLACK)),
	BLACK_KING("K", position -> new King(Position.of(position), Team.BLACK));

	private String symbol;
	private Function<String, Piece> generate;

	PieceConverter(String symbol, Function<String, Piece> generate) {
		this.symbol = symbol;
		this.generate = generate;
	}

	public static Piece of(String symbol, String position) {
		PieceConverter pieceConverter = Arrays.stream(values())
			.filter(piece -> piece.symbol.equals(symbol))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("해당 말이 존재하지 않습니다."));
		return pieceConverter.generate.apply(position);
	}
}
