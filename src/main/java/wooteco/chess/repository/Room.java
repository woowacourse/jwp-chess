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

	public Room(String roomName, String board) {
		this.roomName = roomName;
		this.board = board;
		this.turn = "WHITE";
		this.finishFlag = "N";
	}

	public Long getRoomId() {
		return roomId;
	}

	public String getRoomName() {
		return roomName;
	}

	public String getBoard() {
		return board;
	}

	public String getTurn() {
		return turn;
	}

	public String getFinishFlag() {
		return finishFlag;
	}

	@Override
	public String toString() {
		return "Room{" +
				"roomId=" + roomId +
				", roomName='" + roomName + '\'' +
				", board='" + board + '\'' +
				", turn='" + turn + '\'' +
				", finishFlag='" + finishFlag + '\'' +
				'}';
	}
}
