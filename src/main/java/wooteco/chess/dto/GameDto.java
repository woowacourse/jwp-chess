package wooteco.chess.dto;

import wooteco.chess.domain.Status;
import wooteco.chess.domain.Turn;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.position.Position;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GameDto {
	private final List<UnitDto> units;
	private final Turn turn;
	private final Status status;

	public GameDto(Map<Position, Piece> units, Turn turn, Status status) {
		this.units = units.values().stream()
			.map(piece -> new UnitDto(
				piece.getColumnValue(),
				piece.getRowValue(),
				piece.getTeam().name(),
				piece.getSymbol()
			)).collect(Collectors.toList());
		this.turn = turn;
		this.status = status;
	}

	public Turn getTurn() {
		return turn;
	}

	public List<UnitDto> getUnits() {
		return Collections.unmodifiableList(units);
	}
}
