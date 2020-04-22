package wooteco.chess.dto;

import java.util.Objects;

import wooteco.chess.domain.board.PieceFactory;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.position.Position;

public class PieceDto {
	private Position position;
	private Piece piece;

	private PieceDto(Position position, Piece piece) {
		this.position = position;
		this.piece = piece;
	}

	public static PieceDto of(String position, String pieceName) {
		return new PieceDto(Position.of(position), PieceFactory.of(pieceName));
	}

	public static PieceDto of(Position position, Piece piece) {
		return new PieceDto(position, piece);
	}

	public String getPosition() {
		return position.getValue();
	}

	public String getPiece() {
		return piece.getName();
	}

	public String getPositionValue() {
		return position.getValue();
	}

	public String getPieceName() {
		return piece.getName();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		PieceDto pieceDto = (PieceDto)o;
		return Objects.equals(position, pieceDto.position) &&
			Objects.equals(piece, pieceDto.piece);
	}

	@Override
	public int hashCode() {
		return Objects.hash(position, piece);
	}
}
