package chess.domain.piece;

import static chess.util.NullValidator.*;

import java.util.List;

import chess.domain.board.Position;
import chess.domain.piece.pathStrategy.PathStrategy;
import chess.exception.NotMovableException;

public abstract class Piece {
	protected final PieceColor pieceColor;
	private final String name;
	private final double score;
	private final PathStrategy pathStrategy;
	protected Position position;

	public Piece(String name, double score, PieceColor pieceColor, Position position, PathStrategy pathStrategy) {
		validateNull(name, score, pieceColor, position, pathStrategy);
		this.name = name;
		this.score = score;
		this.pieceColor = pieceColor;
		this.position = position;
		this.pathStrategy = pathStrategy;
	}

	public boolean isNotPawn() {
		return !isPawn();
	}

	public boolean isPawn() {
		return this instanceof Pawn;
	}

	public boolean isNone() {
		return pieceColor.isNoneColor();
	}

	public String getPieceColorName() {
		return this.pieceColor.getName();
	}

	public boolean isSameColor(Piece piece) {
		validateNull(piece);
		return this.pieceColor == piece.pieceColor;
	}

	public boolean isSameColor(PieceColor color) {
		return this.pieceColor == color;
	}

	public String getName() {
		return pieceColor.getPieceName(name);
	}

	public List<Position> getPathTo(Position targetPosition) {
		validateNull(targetPosition);
		validateEqualPosition(targetPosition);
		return pathStrategy.createPaths(this.position, targetPosition);
	}

	private void validateEqualPosition(Position targetPosition) {
		if (this.position.equals(targetPosition)) {
			throw new NotMovableException(String.format("현재 자리한 위치(%s)로는 이동할 수 없습니다.", targetPosition.getName()));
		}
	}

	public void changeTo(Position position) {
		this.position = position;
	}

	public Position getPosition() {
		return position;
	}

	public double getScore() {
		return score;
	}
}
