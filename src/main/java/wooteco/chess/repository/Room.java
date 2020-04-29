package wooteco.chess.repository;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("room")
public class Room {
	@Id
	private Long roomId;
	private String roomName;
	private String board;
	private String turn;
	private String finishFlag;

	public String getRoomName() {
		return roomName;
	}
}
