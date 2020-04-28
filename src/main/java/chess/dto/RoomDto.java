package chess.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("room")
public class RoomDto {
	@Id
	private final int id;
	@Column("room_name")
	private final String roomName;

	public RoomDto(final int id, final String roomName) {
		this.id = id;
		this.roomName = roomName;
	}

	public int getId() {
		return id;
	}

	public String getRoomName() {
		return roomName;
	}
}