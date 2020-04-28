package chess.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("state")
public class StateDto {
	@Id
	final int id;
	@Column("state")
	final String state;
	@Column("room_id")
	final int roomId;

	public StateDto(final int id, final String state, final int roomId) {
		this.id = id;
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
