package wooteco.chess.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("room")
public class RoomDto2 {
	@Id
	@Column("room_id")
	private Long id;
	@Column("turn")
	private final Long turnId;
	private final Long player1Id;
	private final Long player2Id;

	public RoomDto2(Long turnId, Long player1Id, Long player2Id) {
		this.turnId = turnId;
		this.player1Id = player1Id;
		this.player2Id = player2Id;
	}

	public Long getId() {
		return id;
	}

	public Long getTurnId() {
		return turnId;
	}

	public Long getPlayer1Id() {
		return player1Id;
	}

	public Long getPlayer2Id() {
		return player2Id;
	}
}
