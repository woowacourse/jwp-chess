package wooteco.chess.domain.dto;

import java.util.HashMap;
import java.util.Map;

import wooteco.chess.domain.ChessGame;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.position.Column;
import wooteco.chess.domain.position.Row;

public class ChessBoardDto {
	private static final String BLANK = "blank";

	private Map<String, String> board;
	private String turn;

	public ChessBoardDto(Map<String, String> board, String turn) {
		this.board = board;
		this.turn = turn;
	}

	public static ChessBoardDto of(ChessGame chessGame) {
		Map<String, String> board = makeStringBoard(chessGame);
		String turn = chessGame.getTurn().name();
		return new ChessBoardDto(board, turn);
	}

	private static Map<String, String> makeStringBoard(ChessGame chessGame) {
		Map<String, String> board = new HashMap<>();
		for (Column column : Column.values()) {
			for (Row row : Row.values()) {
				board.put(column.getSymbol() + row.getSymbol(), BLANK);
			}
		}

		for (Piece piece : chessGame.getBoard().getPieces()) {
			board.replace(piece.getPosition().toString(), makeName(piece));
		}
		return board;
	}

	private static String makeName(Piece piece) {
		String name = piece.getName() + "_" + piece.getSide();
		return name.toLowerCase();
	}

	public Map<String, String> getBoard() {
		return board;
	}

	public String getTurn() {
		return turn;
	}
}
