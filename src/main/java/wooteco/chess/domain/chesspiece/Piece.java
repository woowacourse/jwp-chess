package wooteco.chess.domain.chesspiece;

import java.util.Objects;
import java.util.function.Function;

import wooteco.chess.domain.MoveManager;
import wooteco.chess.domain.Team;
import wooteco.chess.domain.position.Position;
import wooteco.chess.domain.position.Positions;

public abstract class Piece {
	protected final Team team;
	final MoveManager moveManager;
	protected Position position;

	public Piece(Position position, Team team) {
		this.position = position;
		this.team = team;
		this.moveManager = new MoveManager(this.position);
	}

	public boolean isMatchTeam(Team team) {
		return this.team == team;
	}

	public boolean isNotMatchTeam(Team team) {
		return !(this.team == team);
	}

	public boolean isBlankPiece() {
		return this.team == null;
	}

	public boolean isNotBlankPiece() {
		return this.team != null;
	}

	public boolean equalsPosition(Position position) {
		return this.position.equals(position);
	}

	public boolean isSameTeam(Piece piece) {
		return piece.isMatchTeam(this.team);
	}

	public void changePosition(Position position) {
		this.position = position;
		this.moveManager.changePosition(position);
	}

	public void canMove(Piece piece, Function<Position, Piece> findByPosition) {
		if (isNotNeedCheckPath()) {
			validateCanGo(piece);
			return;
		}
		Positions positions = makePathAndValidate(piece);
		positions.validateCanMovePath(findByPosition);
	}

	public String getPosition() {
		return position.getString();
	}

	public abstract boolean isNotNeedCheckPath();

	public abstract void validateCanGo(Piece targetPiece);

	public abstract Positions makePathAndValidate(Piece targetPiece);

	public abstract String getName();

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Piece that = (Piece)o;
		return team == that.team &&
			position.equals(that.position);
	}

	@Override
	public int hashCode() {
		return Objects.hash(team, position);
	}
}