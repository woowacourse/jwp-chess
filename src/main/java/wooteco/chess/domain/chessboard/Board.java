package wooteco.chess.domain.chessboard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import wooteco.chess.domain.Status;
import wooteco.chess.domain.Team;
import wooteco.chess.domain.Turn;
import wooteco.chess.domain.chesspiece.Blank;
import wooteco.chess.domain.chesspiece.King;
import wooteco.chess.domain.chesspiece.Piece;
import wooteco.chess.domain.position.Position;

public class Board {
	private static final String CANNOT_MOVE_PATH = "이동할 수 없는 경로 입니다.";
	private static final String SAME_TEAM_MESSAGE = "같은 팀입니다.";
	private static final String NOT_CHESS_PIECE_MESSAGE = "체스 말이 아닙니다.";

	private final List<Row> rows;
	private final Turn turn;

	public Board(List<Row> rows, Turn turn) {
		this.rows = new ArrayList<>(rows);
		this.turn = turn;
	}

	public boolean isLiveBothKing() {
		return isLiveKing(Team.BLACK) && isLiveKing(Team.WHITE);
	}

	public boolean isLiveKing(Team team) {
		return findByTeam(team).stream()
			.anyMatch(chessPiece -> chessPiece.getClass() == King.class);
	}

	public List<Piece> findAll() {
		return rows.stream()
			.map(Row::getPieces)
			.flatMap(Collection::stream)
			.collect(Collectors.toList());
	}

	private List<Piece> findByTeam(Team team) {
		return rows.stream()
			.map(row -> row.findByTeam(team))
			.flatMap(Collection::stream)
			.collect(Collectors.toList());
	}

	public void move(Position source, Position target) {
		Piece startPiece = findByPosition(source);
		turn.validateTurn(startPiece);
		Piece targetPiece = findByPosition(target);

		checkTeam(startPiece, targetPiece);
		startPiece.canMove(targetPiece, this::findByPosition);

		replace(source, new Blank(source));
		replace(target, startPiece);

		turn.changeTurn();
	}

	public Piece findByPosition(Position position) {
		return rows.stream()
			.filter(row -> row.contains(position))
			.map(row -> row.findByPosition(position))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException(CANNOT_MOVE_PATH));
	}

	private void checkTeam(Piece source, Piece target) {
		validateNotBlank(source);
		validateOtherTeam(source, target);
	}

	private void validateNotBlank(Piece source) {
		if (source.isBlankPiece()) {
			throw new IllegalArgumentException(NOT_CHESS_PIECE_MESSAGE);
		}
	}

	private void validateOtherTeam(Piece source, Piece target) {
		if (source.isSameTeam(target)) {
			throw new IllegalArgumentException(SAME_TEAM_MESSAGE);
		}
	}

	private void replace(Position targetPosition, Piece source) {
		Row row = findRow(targetPosition);
		row.replace(targetPosition, source);
	}

	private Row findRow(Position targetPosition) {
		return rows.stream()
			.filter(row -> row.contains(targetPosition))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException(CANNOT_MOVE_PATH));
	}

	public Status createStatus() {
		return new Status(rows);
	}

	public boolean isWhiteTurn() {
		return turn.isWhiteTurn();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Board that = (Board)o;
		return Objects.equals(rows, that.rows) &&
			Objects.equals(turn, that.turn);
	}

	@Override
	public int hashCode() {
		return Objects.hash(rows, turn);
	}

	public List<Row> getRows() {
		return Collections.unmodifiableList(rows);
	}

}