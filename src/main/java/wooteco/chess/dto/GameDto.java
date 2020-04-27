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
	private final List<PieceDto> pieces;
	private final Turn turn;
	private final Status status;

	public GameDto(Map<Position, Piece> pieces, Turn turn, Status status) {
		this.pieces = pieces.entrySet().stream()
				.map(entry -> {
					Position position = entry.getKey();
					Piece piece = entry.getValue();
					return new PieceDto(position, piece.getTeam().name(), piece.getSymbol());
				}).collect(Collectors.toList());
		this.turn = turn;
		this.status = status;
	}

	public Turn getTurn() {
		return turn;
	}

	public List<PieceDto> getPieces() {
		return Collections.unmodifiableList(pieces);
	}

	public Status getStatus() {
		return status;
	}
}
