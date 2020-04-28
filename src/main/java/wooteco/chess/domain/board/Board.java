package wooteco.chess.domain.board;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;

import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.Team;
import wooteco.chess.domain.position.Path;
import wooteco.chess.domain.position.Position;

public class Board {
	private static final int ALIVE_COUNT = 2;

	@Id
	private Long id;

	private final List<Piece> pieces;

	private Board(List<Piece> pieces) {
		this.pieces = pieces;
	}

	public static Board of(List<Piece> pieces) {
		return new Board(pieces);
	}

	public static Board of(Map<Position, Piece> board) {
		return new Board(new ArrayList<>(board.values()));
	}

	public void verifyMove(Position from, Position to, Team current) {
		if (isEnd()) {
			throw new IllegalArgumentException("게임 끝");
		}

		Piece piece = findPieceIn(from);
		Piece target = findPieceIn(to);

		if (piece.isNotSameTeam(current)) {
			throw new IllegalArgumentException("아군 기물의 위치가 아닙니다.");
		}
		if (hasPieceIn(Path.of(from, to)) || piece.canNotMoveTo(target)) {
			throw new IllegalArgumentException("이동할 수 없는 경로입니다.");
		}
	}

	private Piece findPieceIn(Position position) {
		return pieces.stream()
				.filter(piece -> piece.isPositionEquals(position))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("해당 위치에 기물이 존재하지 않습니다. position = " + position.getName()));
	}

	private boolean hasPieceIn(Path path) {
		return path.toList()
			.stream()
			.anyMatch(position -> findPieceIn(position).isObstacle());
	}

	private boolean isEnd() {
		return pieces.stream()
			.filter(Piece::hasToAlive)
			.count() != ALIVE_COUNT;
	}
}
