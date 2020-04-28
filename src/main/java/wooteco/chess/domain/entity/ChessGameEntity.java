package wooteco.chess.domain.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import wooteco.chess.domain.piece.Team;

@Table("chessGame")
public class ChessGameEntity {
	@Id
	private Long id;

	@Column("room_id")
	private String roomId;

	@Column("turn")
	private String turn;

	public ChessGameEntity(String roomId, String turn) {
		this.roomId = roomId;
		this.turn = turn;
	}

	public String getTurn() {
		return turn;
	}

	public void updateTurnToNext() {
		turn = Team.valueOf(turn).next().name();
	}
}
