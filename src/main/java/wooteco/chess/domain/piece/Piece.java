package wooteco.chess.domain.piece;

import java.util.Objects;
import java.util.Optional;

import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.board.InvalidTurnException;
import wooteco.chess.domain.piece.position.Direction;
import wooteco.chess.domain.piece.position.InvalidPositionException;
import wooteco.chess.domain.piece.position.Position;
import wooteco.chess.domain.piece.team.Team;

public abstract class Piece {
	protected Position position;
	protected Team team;

	public Piece(Position position, Team team) {
		this.position = position;
		this.team = team;
	}

	public void move(Position targetPosition, Team turn, Board board) {
		validateMovement(targetPosition, turn, board);

		Optional<Piece> targetPiece = board.findPiece(targetPosition);
		targetPiece.ifPresent(target -> capture(target, board));
		this.changePosition(targetPosition, board);
	}

	protected void validateMovement(Position targetPosition, Team turn, Board board) {
		validateTurn(turn);
		validateIsInPlace(targetPosition);
		Direction direction = Direction.findDirection(this.position, targetPosition);
		validateDirection(direction);
		validateStepSize(this.position, targetPosition);
		validateRoute(direction, targetPosition, board);
	}

	private void validateTurn(Team nowTurn) {
		boolean isNotThisTurn = !this.team.equals(nowTurn);

		if (isNotThisTurn) {
			throw new InvalidTurnException(InvalidTurnException.INVALID_TURN);
		}
	}

	private void validateIsInPlace(Position targetPosition) {
		boolean isInPlace = this.position.equals(targetPosition);

		if (isInPlace) {
			throw new InvalidPositionException(InvalidPositionException.IS_IN_PLACE);
		}
	}

	protected void capture(Piece target, Board board) {
		if (team.isOurTeam(target.team)) {
			throw new InvalidPositionException(InvalidPositionException.HAS_OUR_TEAM_AT_TARGET_POSITION);
		}
		board.remove(target.getRankIndex(), target);
	}

	protected void changePosition(Position targetPosition, Board board) {
		board.remove(getRankIndex(), this);
		this.position = targetPosition;
		board.add(targetPosition.getRankIndex(), this);
	}

	public boolean isTeam(Team team) {
		return this.team.equals(team);
	}

	public boolean equalsColumn(int columnNumber) {
		return position.getColumnNumber() == columnNumber;
	}

	public String getSymbol() {
		if (Team.isWhite(team)) {
			return getRawSymbol();
		}
		return getRawSymbol().toUpperCase();
	}

	public Team getTeam() {
		return team;
	}

	public Position getPosition() {
		return position;
	}

	public int getRankIndex() {
		return position.getRankIndex();
	}

	protected abstract void validateDirection(Direction direction);

	protected abstract void validateStepSize(Position sourcePosition, Position targetPosition);

	protected abstract void validateRoute(Direction direction, Position targetPosition, Board ranks);

	public abstract double getScore();

	protected abstract String getRawSymbol();

	@Override
	public int hashCode() {
		return Objects.hash(position, team);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Piece piece = (Piece)o;
		return Objects.equals(position, piece.position) &&
			team == piece.team;
	}
}
