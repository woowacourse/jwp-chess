package wooteco.chess.dto;

import java.util.Map;

import wooteco.chess.domain.GameManager;
import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.piece.Color;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.position.Position;

public class GameManagerDto {
	private final String board;
	private final Color turn;

	public GameManagerDto(GameManager gameManager) {
		this.board = parseString(gameManager.getBoard());
		this.turn = gameManager.getCurrentTurn();
	}

	private String parseString(Board board) {
		Map<Position, Piece> pieces = board.getPieces();
		StringBuilder parsedBoard = new StringBuilder();
		for (int i = 1; i <= 8; i++) {
			parseOneColumn(pieces, parsedBoard, i);
		}
		return parsedBoard.toString();
	}

	private void parseOneColumn(Map<Position, Piece> pieces, StringBuilder parsedBoard, int i) {
		for (char c = 'A'; c <= 'H'; c++) {
			parsedBoard.append(parseOneCell(pieces, Position.of(String.valueOf(c) + String.valueOf(i))));
		}
	}

	private String parseOneCell(Map<Position, Piece> pieces, Position position) {
		Piece piece = pieces.getOrDefault(position, null);
		if (piece == null) {
			return ".";
		}
		return piece.getName();
	}

	public String getBoard() {
		return board;
	}

	public String getTurn() {
		return turn.name();
	}
}
