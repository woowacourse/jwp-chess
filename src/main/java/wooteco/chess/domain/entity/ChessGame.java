package wooteco.chess.domain.entity;

import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import wooteco.chess.domain.piece.Turn;

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

	public Long getId() {
		return id;
	}
}
