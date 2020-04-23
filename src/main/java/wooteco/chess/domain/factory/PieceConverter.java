package wooteco.chess.domain.factory;

import java.util.Arrays;
import java.util.function.Function;

import wooteco.chess.domain.Team;
import wooteco.chess.domain.chesspiece.Bishop;
import wooteco.chess.domain.chesspiece.Blank;
import wooteco.chess.domain.chesspiece.King;
import wooteco.chess.domain.chesspiece.Knight;
import wooteco.chess.domain.chesspiece.Pawn;
import wooteco.chess.domain.chesspiece.Piece;
import wooteco.chess.domain.chesspiece.Queen;
import wooteco.chess.domain.chesspiece.Rook;
import wooteco.chess.domain.position.Position;

public enum PieceConverter {
	BISHOP_BLACK("B", (position) -> new Bishop(Position.of(position), Team.BLACK)),
	BISHOP_WHITE("b", (position) -> new Bishop(Position.of(position), Team.WHITE)),
	KING_BLACK("K", (position) -> new King(Position.of(position), Team.BLACK)),
	KING_WHITE("k", (position) -> new King(Position.of(position), Team.WHITE)),
	KNIGHT_BLACK("N", (position) -> new Knight(Position.of(position), Team.BLACK)),
	KNIGHT_WHITE("n", (position) -> new Knight(Position.of(position), Team.WHITE)),
	PAWN_BLACK("P", (position) -> new Pawn(Position.of(position), Team.BLACK)),
	PAWN_WHITE("p", (position) -> new Pawn(Position.of(position), Team.WHITE)),
	QUEEN_BLACK("Q", (position) -> new Queen(Position.of(position), Team.BLACK)),
	QUEEN_WHITE("q", (position) -> new Queen(Position.of(position), Team.WHITE)),
	ROOK_BLACK("R", (position) -> new Rook(Position.of(position), Team.BLACK)),
	ROOK_WHITE("r", (position) -> new Rook(Position.of(position), Team.WHITE)),
	BLANK(".", (position) -> new Blank(Position.of(position)));

	private static final String NOT_EXIST_NAME_MESSAGE = "존재하지 않는 Piece 이름 입니다.";

	private String name;
	private Function<String, Piece> creator;

	PieceConverter(String name, Function<String, Piece> creator) {
		this.name = name;
		this.creator = creator;
	}

	public static Piece convert(String position, String pieceName) {
		System.out.println("여기");
		System.out.println(position + " " + pieceName);
		return Arrays.stream(values())
			.filter(converter -> converter.name.equals(pieceName))
			.findFirst()
			.map(converter -> converter.creator.apply(position))
			.orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_NAME_MESSAGE));
	}
}
