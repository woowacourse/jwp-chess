package wooteco.chess.domain.piece;

import static wooteco.chess.util.NullValidator.*;

import java.util.List;
import java.util.Objects;

import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.piece.pathStrategy.LongRangePieceStrategy;
import wooteco.chess.domain.piece.pathStrategy.PathStrategy;
import wooteco.chess.exception.NotMovableException;

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

	public boolean hasLongRangePieceStrategy() {
		return this.pathStrategy instanceof LongRangePieceStrategy;
	}

	public Position getPosition() {
		return position;
	}

	public double getScore() {
		return score;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Piece piece = (Piece)o;
		return Double.compare(piece.score, score) == 0 &&
			pieceColor == piece.pieceColor &&
			Objects.equals(name, piece.name) &&
			Objects.equals(position, piece.position);
	}

	@Override
	public int hashCode() {
		return Objects.hash(pieceColor, name, score, pathStrategy, position);
	}
}
