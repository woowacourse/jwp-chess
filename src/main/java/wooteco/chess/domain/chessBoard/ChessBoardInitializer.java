package wooteco.chess.domain.chessBoard;

import java.util.HashMap;
import java.util.Map;

import wooteco.chess.domain.chessPiece.ChessPiece;
import wooteco.chess.domain.position.ChessFile;
import wooteco.chess.domain.position.ChessRank;
import wooteco.chess.domain.position.Position;
import wooteco.chess.domain.chessPiece.pieceType.PieceType;

public class ChessBoardInitializer {

	private static final int WHITE_OTHERS_INIT_RANK = 1;
	private static final int WHITE_PAWN_INIT_RANK = 2;
	private static final int BLACK_PAWN_INIT_RANK = 7;
	private static final int BLACK_OTHERS_INIT_RANK = 8;

	public static Map<Position, ChessPiece> create() {
		final Map<Position, ChessPiece> chessBoard = new HashMap<>();

		setWhitePawn(chessBoard);
		setWhiteOthersBy(chessBoard);
		setBlackPawn(chessBoard);
		setBlackOthersBy(chessBoard);
		return chessBoard;
	}

	private static void setWhitePawn(final Map<Position, ChessPiece> chessBoard) {
		for (ChessFile chessFile : ChessFile.values()) {
			chessBoard.put(Position.of(chessFile, ChessRank.from(WHITE_PAWN_INIT_RANK)), new ChessPiece(
				PieceType.WHITE_PAWN));
		}
	}

	private static void setBlackPawn(final Map<Position, ChessPiece> chessBoard) {
		for (ChessFile chessFile : ChessFile.values()) {
			chessBoard.put(Position.of(chessFile, ChessRank.from(BLACK_PAWN_INIT_RANK)), new ChessPiece(
				PieceType.BLACK_PAWN));
		}
	}

	private static void setWhiteOthersBy(final Map<Position, ChessPiece> chessBoard) {
		chessBoard.put(Position.of(ChessFile.A, ChessRank.from(WHITE_OTHERS_INIT_RANK)), new ChessPiece(
			PieceType.WHITE_ROOK));
		chessBoard.put(Position.of(ChessFile.B, ChessRank.from(WHITE_OTHERS_INIT_RANK)), new ChessPiece(
			PieceType.WHITE_KNIGHT));
		chessBoard.put(Position.of(ChessFile.C, ChessRank.from(WHITE_OTHERS_INIT_RANK)), new ChessPiece(
			PieceType.WHITE_BISHOP));
		chessBoard.put(Position.of(ChessFile.D, ChessRank.from(WHITE_OTHERS_INIT_RANK)), new ChessPiece(
			PieceType.WHITE_QUEEN));
		chessBoard.put(Position.of(ChessFile.E, ChessRank.from(WHITE_OTHERS_INIT_RANK)), new ChessPiece(
			PieceType.WHITE_KING));
		chessBoard.put(Position.of(ChessFile.F, ChessRank.from(WHITE_OTHERS_INIT_RANK)), new ChessPiece(
			PieceType.WHITE_BISHOP));
		chessBoard.put(Position.of(ChessFile.G, ChessRank.from(WHITE_OTHERS_INIT_RANK)), new ChessPiece(
			PieceType.WHITE_KNIGHT));
		chessBoard.put(Position.of(ChessFile.H, ChessRank.from(WHITE_OTHERS_INIT_RANK)), new ChessPiece(
			PieceType.WHITE_ROOK));
	}

	private static void setBlackOthersBy(final Map<Position, ChessPiece> chessBoard) {
		chessBoard.put(Position.of(ChessFile.A, ChessRank.from(BLACK_OTHERS_INIT_RANK)), new ChessPiece(
			PieceType.BLACK_ROOK));
		chessBoard.put(Position.of(ChessFile.B, ChessRank.from(BLACK_OTHERS_INIT_RANK)), new ChessPiece(
			PieceType.BLACK_KNIGHT));
		chessBoard.put(Position.of(ChessFile.C, ChessRank.from(BLACK_OTHERS_INIT_RANK)), new ChessPiece(
			PieceType.BLACK_BISHOP));
		chessBoard.put(Position.of(ChessFile.D, ChessRank.from(BLACK_OTHERS_INIT_RANK)), new ChessPiece(
			PieceType.BLACK_QUEEN));
		chessBoard.put(Position.of(ChessFile.E, ChessRank.from(BLACK_OTHERS_INIT_RANK)), new ChessPiece(
			PieceType.BLACK_KING));
		chessBoard.put(Position.of(ChessFile.F, ChessRank.from(BLACK_OTHERS_INIT_RANK)), new ChessPiece(
			PieceType.BLACK_BISHOP));
		chessBoard.put(Position.of(ChessFile.G, ChessRank.from(BLACK_OTHERS_INIT_RANK)), new ChessPiece(
			PieceType.BLACK_KNIGHT));
		chessBoard.put(Position.of(ChessFile.H, ChessRank.from(BLACK_OTHERS_INIT_RANK)), new ChessPiece(
			PieceType.BLACK_ROOK));
	}

}
