package wooteco.chess.dto;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import wooteco.chess.domain.Status;
import wooteco.chess.domain.Turn;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.position.Position;

public class GameDto {
	private final List<PieceDto> units;
	private final Turn turn;
	private final Status status2;

	public GameDto(Map<Position, Piece> units, Turn turn, Status status2) {
		this.units = units.values().stream()
			.map(piece -> new PieceDto(
				piece.getColumnValue(),
				piece.getRowValue(),
				piece.getTeam().name(),
				piece.getSymbol()
			)).collect(Collectors.toList());
		this.turn = turn;
		this.status2 = status2;
	}

	public Turn getTurn() {
		return turn;
	}

	public List<PieceDto> getUnits() {
		return Collections.unmodifiableList(units);
	}

	public Status getStatus2() {
		return status2;
	}
}
