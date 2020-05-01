package chess.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("state")
public class StateDto {
	@Id
	private int id;
	@Column("state")
	private String state;
	@Column("room_id")
	private int roomId;

	public StateDto(final String state, final int roomId) {
		this.state = state;
		this.roomId = roomId;
	}

	public int getId() {
		return id;
	}

	public String getState() {
		return state;
	}

	public int getRoomId() {
		return roomId;
	}
}
