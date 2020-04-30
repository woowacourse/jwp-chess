package chess.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("announcement")
public class AnnouncementDto {
	@Id
	private int id;
	private final String message;
	private final int roomId;

	public AnnouncementDto(final String message, final int roomId) {
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
