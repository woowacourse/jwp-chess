package wooteco.chess.repository;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import wooteco.chess.domain.Side;

@Table("room")
public class Room {
	@Id
	private Long roomId;
	private String roomName;
	private String board;
	private Side turn;
	private String finishFlag;

	public Room(String roomName, String board, Side turn, String finishFlag) {
		this.roomName = roomName;
		this.board = board;
		this.turn = turn;
		this.finishFlag = finishFlag;
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

	public Side getTurn() {
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

	public void update(String board, Side turn, String finishFlag) {
		this.board = board;
		this.turn = turn;
		this.finishFlag = finishFlag;
	}
}
