package wooteco.chess.domain.board;

import java.util.Map;

import wooteco.chess.domain.piece.Color;
import wooteco.chess.domain.piece.King;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.position.Position;

public class Board {
	private final Map<Position, Piece> pieces;

	public Board(Map<Position, Piece> pieces) {
		this.pieces = pieces;
	}

	public boolean isNotEmptyPosition(Position position) {
		return pieces.get(position) != null;
	}

	public Piece findPieceBy(Position position) {
		return pieces.get(position);
	}

	public void movePiece(Position from, Position to) {
		Piece target = pieces.remove(from);
		pieces.put(to, target);
	}

	public Map<Position, Piece> getPieces() {
		return pieces;
	}

	public boolean isKingAliveOf(Color color) {
		return pieces.values().stream()
			.anyMatch(piece -> isKingOf(color, piece));
	}

	private boolean isKingOf(Color color, Piece piece) {
		return piece.isSameColor(color) && piece instanceof King;
	}

	public void deleteAll() {

	}
}