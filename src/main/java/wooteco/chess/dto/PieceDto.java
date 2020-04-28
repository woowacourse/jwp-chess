package wooteco.chess.dto;

import wooteco.chess.domain.chesspiece.Piece;
import wooteco.chess.domain.position.Position;

public class PieceDto {
	private final Position position;
	private final String name;

	private PieceDto(Position position, String name) {
		this.position = position;
		this.name = name;
	}

	public static PieceDto from(Piece piece) {
		return new PieceDto(piece.getPosition(), piece.getName());
	}

	public String getName() {
		return name;
	}

	public String getPosition() {
		return position.getString();
	}
}
