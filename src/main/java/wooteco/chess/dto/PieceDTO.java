package wooteco.chess.dto;

import wooteco.chess.domain.chesspiece.Piece;
import wooteco.chess.domain.position.Position;

public class PieceDTO {
	private final Position position;
	private final String name;

	private PieceDTO(Position position, String name) {
		this.position = position;
		this.name = name;
	}

	public static PieceDTO from(Piece piece) {
		return new PieceDTO(piece.getPosition(), piece.getName());
	}

	public String getName() {
		return name;
	}

	public String getPosition() {
		return position.getString();
	}
}
