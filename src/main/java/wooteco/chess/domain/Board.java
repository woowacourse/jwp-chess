package wooteco.chess.domain;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.blank.Blank;
import wooteco.chess.domain.piece.king.King;
import wooteco.chess.domain.piece.pawn.Pawn;
import wooteco.chess.domain.position.Position;

public class Board {
	private final Map<Position, Piece> board;

	public Board(Map<Position, Piece> board) {
		this.board = new TreeMap<>(board);
	}

	public boolean isKingDead() {
		return board.entrySet().stream()
			.filter(entry -> entry.getValue() instanceof King)
			.count() != 2;
	}

	public void move(Position from, Position to, Turn turn) {
		Piece source = hasPieceIn(from);
		checkTurn(turn, source);
		source = source.move(from, to, getTeamBoard());
		board.remove(from);
		board.put(from, new Blank(from));
		board.put(to, source);
	}

	private void checkTurn(Turn turn, Piece source) {
		if (!source.isTurn(turn)) {
			throw new IllegalArgumentException("해당 플레이어의 턴이 아닙니다.");
		}
	}

	private Piece hasPieceIn(Position from) {
		Piece piece = board.get(from);
		if (Objects.isNull(piece)) {
			throw new IllegalArgumentException("해당 위치에 기물이 존재하지 않습니다.");
		}
		return piece;
	}

	public List<Piece> findPiecesByTeam(Team team) {
		return board.values().stream()
			.filter(value -> value.getTeam() == team)
			.collect(Collectors.toList());
	}

	public Map<Position, Piece> getBoard() {
		return Collections.unmodifiableMap(board);
	}

	public Map<Position, Team> getTeamBoard() {
		return board.entrySet().stream()
			.filter(entry -> entry.getValue().isNotNone())
			.collect(Collectors.toMap(
				Map.Entry::getKey,
				entry -> entry.getValue().getTeam()
			));
	}
}
