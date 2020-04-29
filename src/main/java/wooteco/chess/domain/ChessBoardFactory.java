package wooteco.chess.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import wooteco.chess.domain.piece.Bishop;
import wooteco.chess.domain.piece.King;
import wooteco.chess.domain.piece.Knight;
import wooteco.chess.domain.piece.Pawn;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.Queen;
import wooteco.chess.domain.piece.Rook;
import wooteco.chess.domain.position.Column;
import wooteco.chess.domain.position.Position;
import wooteco.chess.domain.position.Row;

public class ChessBoardFactory {
	private ChessBoardFactory() {
	}

	public static ChessBoard create() {
		List<Piece> chessBoard = new ArrayList<>();
		chessBoard.addAll(createNoble(Side.BLACK));
		chessBoard.addAll(createPawn(Side.BLACK));
		chessBoard.addAll(createNoble(Side.WHITE));
		chessBoard.addAll(createPawn(Side.WHITE));
		return new ChessBoard(chessBoard);
	}

	private static List<Piece> createPawn(Side side) {
		return Arrays.stream(Column.values())
				.map(col -> new Pawn(side, new Position(col, side.getInitPawnRow())))
				.collect(Collectors.toList());
	}

	private static List<Piece> createNoble(Side side) {
		Row row = side.getInitNobleRow();
		return Arrays.asList(
				new Rook(side, new Position("a", row)),
				new Knight(side, new Position("b", row)),
				new Bishop(side, new Position("c", row)),
				new Queen(side, new Position("d", row)),
				new King(side, new Position("e", row)),
				new Bishop(side, new Position("f", row)),
				new Knight(side, new Position("g", row)),
				new Rook(side, new Position("h", row))
		);
	}
}
