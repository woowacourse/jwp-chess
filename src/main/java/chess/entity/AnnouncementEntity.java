package chess.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("announcement")
public class AnnouncementEntity {
	@Id
	private int id;
	private String message;
	private int roomId;

	public AnnouncementEntity(final String message, final int roomId) {
		this.message = message;
		this.roomId = roomId;
	}

	public int getId() {
		return id;
	}

	public String getMessage() {
		return message;
	}

	public int getRoomId() {
		return roomId;
	}
}
