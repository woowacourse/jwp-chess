package chess.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("announcement")
public class AnnouncementDto {
	@Id
	private final int id;
	private final String announcement;
	private final int roomId;

	public AnnouncementDto(final int id, final String announcement, final int roomId) {
		this.id = id;
		this.announcement = announcement;
		this.roomId = roomId;
	}

	public int getId() {
		return id;
	}

	public String getMessage() {
		return announcement;
	}

	public int getRoomId() {
		return roomId;
	}
}
