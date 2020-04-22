package wooteco.chess.domain.chessboard;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import wooteco.chess.domain.Team;
import wooteco.chess.domain.chesspiece.Pawn;
import wooteco.chess.domain.chesspiece.Piece;
import wooteco.chess.domain.position.Position;

public class Row {
	private static final String NOT_MATCH_POSITION_MESSAGE = "해당 행에 존재하지 않는 좌표 입니다.";
	private static final String NOT_MATCH_CHESS_PIECE_MESSAGE = "목표 위치와 일치하는 ChessPiece가 없습니다.";

	private List<Piece> pieces;

	public Row(List<Piece> pieces) {
		this.pieces = pieces;
	}

	public boolean contains(Position position) {
		return pieces.stream()
			.anyMatch(chessPiece -> chessPiece.equalsPosition(position));
	}

	public Piece findByPosition(Position position) {
		return pieces.stream()
			.filter(chessPiece -> chessPiece.equalsPosition(position))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException(NOT_MATCH_POSITION_MESSAGE));
	}

	public void replace(Position targetPosition, Piece startPiece) {
		int targetIndex = IntStream.range(0, pieces.size())
			.filter(index -> pieces.get(index).equalsPosition(targetPosition))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException(NOT_MATCH_CHESS_PIECE_MESSAGE));

		pieces.set(targetIndex, startPiece);
		startPiece.changePosition(targetPosition);
	}

	public List<Piece> findByTeam(Team team) {
		List<Piece> pieces = this.pieces.stream()
			.filter(chessPiece -> chessPiece.isMatchTeam(team))
			.collect(Collectors.toList());
		return Collections.unmodifiableList(pieces);
	}

	public boolean isPawn(int index, Team team) {
		Piece piece = pieces.get(index);
		return piece.isMatchTeam(team) && piece.getClass() == Pawn.class;
	}

	public List<Piece> getPieces() {
		return Collections.unmodifiableList(pieces);
	}

	public Piece get(int x) {
		return pieces.get(x);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Row row = (Row)o;
		return pieces.equals(row.pieces);
	}

	@Override
	public int hashCode() {
		return Objects.hash(pieces);
	}
}
