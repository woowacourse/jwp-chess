package wooteco.chess.domain.entity;

import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import wooteco.chess.domain.piece.Turn;
import wooteco.chess.domain.position.Position;

@Table("chess_game")
public class ChessGame {
	@Id
	private Long id;

	private String roomId;

	private Turn turn;

	private Set<PieceEntity> pieces;

	public ChessGame(String roomId, Turn turn, Set<PieceEntity> pieces) {
		this.roomId = roomId;
		this.turn = turn;
		this.pieces = pieces;
	}

	public void updateTurnToNext() {
		turn = turn.next();
	}

	public Turn getTurn() {
		return turn;
	}

	public Set<PieceEntity> getPieces() {
		return pieces;
	}

	public PieceEntity findPieceByPosition(Position position) {
		return pieces.stream()
			.filter(pieceEntity -> pieceEntity.isPositionEquals(position))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("기물이 존재하지 않습니다. position : " + position.getName()));
	}

	public String getRoomId() {
		return roomId;
	}
}
