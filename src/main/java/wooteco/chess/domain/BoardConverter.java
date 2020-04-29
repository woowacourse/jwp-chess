package wooteco.chess.domain;

import static wooteco.chess.dto.ChessGameDto.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.PieceFactory;
import wooteco.chess.domain.position.Position;

public class BoardConverter {
	public static final String BLANK_MARK = ".";
	private static final int BOARD_MIN_SIZE = 0;
	private static final int BOARD_MAX_SIZE = 8;

	public static ChessBoard convertToBoard(String board) {
		List<Piece> pieces = new ArrayList<>();
		int col = BOARD_MIN_SIZE;
		int row = BOARD_MAX_SIZE;

		for (String pieceName : board.split("")) {
			Optional<Piece> piece = PieceFactory.of(pieceName.toLowerCase(), convertToSide(pieceName),
					new Position((col % BOARD_MAX_SIZE) + 1, row));

			if (piece.isPresent()) {
				pieces.add(piece.orElseThrow(NoSuchElementException::new));
			}
			if (++col % BOARD_MAX_SIZE == BOARD_MIN_SIZE) {
				row--;
			}
		}
		return new ChessBoard(pieces);
	}

	public static ChessBoard convertToBoard(Map<String, String> board) {
		List<Piece> pieces = board.entrySet().stream()
				.filter(e -> !e.getValue().equals(BLANK))
				.map(BoardConverter::convertToPiece)
				.collect(Collectors.toList());
		return new ChessBoard(pieces);
	}

	private static Piece convertToPiece(Map.Entry<String, String> entry) {
		String pieceName = entry.getValue().substring(0, 1);
		Optional<Piece> piece;
		if (entry.getValue().substring(2).equals("black")) {
			piece = PieceFactory.of(pieceName, Side.BLACK, new Position(entry.getKey()));
			return piece.orElseThrow(NoSuchElementException::new);
		}
		piece = PieceFactory.of(pieceName, Side.WHITE, new Position(entry.getKey()));
		return piece.orElseThrow(NoSuchElementException::new);
	}

	private static Side convertToSide(String boardName) {
		if (Character.isUpperCase(boardName.charAt(0))) {
			return Side.BLACK;
		}
		return Side.WHITE;
	}

	public static String convertToString(ChessBoard chessBoard) {
		List<List<String>> boardInfo = makeStringBoard(chessBoard.getPieces());

		StringBuilder builder = new StringBuilder();
		for (List<String> strings : boardInfo) {
			for (String string : strings) {
				builder.append(string);
			}
		}
		return builder.toString();
	}

	public static List<List<String>> makeStringBoard(List<Piece> pieces) {
		List<List<String>> board = new ArrayList<>();
		makeEmptyBoard(board);
		deployPieces(pieces, board);
		return board;
	}

	private static void deployPieces(List<Piece> pieces, List<List<String>> board) {
		for (Piece piece : pieces) {
			board.get(BOARD_MAX_SIZE - piece.getPosition().getRow().getSymbol())
					.set(piece.getPosition().getCol().getValue() - 1, piece.getName());
		}
	}

	private static void makeEmptyBoard(List<List<String>> board) {
		for (int i = BOARD_MIN_SIZE; i < BOARD_MAX_SIZE; i++) {
			List<String> emptyRow = new ArrayList<>();
			for (int j = BOARD_MIN_SIZE; j < BOARD_MAX_SIZE; j++) {
				emptyRow.add(BLANK_MARK);
			}
			board.add(emptyRow);
		}
	}
}
