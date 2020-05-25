package wooteco.chess.repository;

import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import wooteco.chess.domain.BoardConverter;
import wooteco.chess.domain.ChessGame;
import wooteco.chess.domain.FinishFlag;
import wooteco.chess.domain.Side;

@Table("room")
public class Room {
	@Id
	private Long roomId;
	private String roomName;
	private String board;
	private Side turn;
	private String finishFlag;

	public Room(String roomName, ChessGame chessGame) {
		this.roomName = roomName;
		this.board = BoardConverter.convertToString(chessGame.getBoard());
		this.turn = chessGame.getTurn();
		this.finishFlag = FinishFlag.of(chessGame.isEnd()).getSymbol();
	}

	public Room(String roomName, String board, Side turn, String finishFlag) {
		this.roomName = roomName;
		this.board = board;
		this.turn = turn;
		this.finishFlag = finishFlag;
	}

	public void update(String board, Side turn, String finishFlag) {
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
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Room room = (Room)o;
		return Objects.equals(roomId, room.roomId) &&
				Objects.equals(roomName, room.roomName) &&
				Objects.equals(board, room.board) &&
				turn == room.turn &&
				Objects.equals(finishFlag, room.finishFlag);
	}

	@Override
	public int hashCode() {
		return Objects.hash(roomId, roomName, board, turn, finishFlag);
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
