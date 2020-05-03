package wooteco.chess.domain;

import java.util.Objects;

import wooteco.chess.domain.chesspiece.Piece;

public class Turn {
	private static final String NOT_THIS_TEAM_TURN_MESSAGE = "해당 팀 차례가 아닙니다.";
	public static final boolean FIRST = true;

	private boolean isWhiteTurn;

	public Turn(boolean isWhiteTurn) {
		this.isWhiteTurn = isWhiteTurn;
	}

	public void validateTurn(Piece piece) {
		if (checkWhiteTeamTurn(piece) || checkBlackTeamTurn(piece)) {
			throw new UnsupportedOperationException(NOT_THIS_TEAM_TURN_MESSAGE);
		}
	}

	private boolean checkWhiteTeamTurn(Piece piece) {
		return isWhiteTurn && piece.isNotMatchTeam(Team.WHITE);
	}

	private boolean checkBlackTeamTurn(Piece piece) {
		return !isWhiteTurn && piece.isNotMatchTeam(Team.BLACK);
	}

	public void changeTurn() {
		this.isWhiteTurn = !this.isWhiteTurn;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Turn turn = (Turn)o;
		return isWhiteTurn == turn.isWhiteTurn;
	}

	@Override
	public int hashCode() {
		return Objects.hash(isWhiteTurn);
	}

	public boolean isWhiteTurn() {
		return isWhiteTurn;
	}
}
