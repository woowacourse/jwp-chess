package chess.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("announcement")
public class AnnouncementDto {
	@Id
	private final int id;
	@Column("message")
	private final String announcement;
	@Column("room_id")
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
