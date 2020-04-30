package chess.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("state")
public class StateEntity {
	@Id
	private int id;
	private String state;
	private int roomId;

	public StateEntity(final String state, final int roomId) {
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
