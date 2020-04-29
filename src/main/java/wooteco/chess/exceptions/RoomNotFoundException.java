package wooteco.chess.exceptions;

public class RoomNotFoundException extends RuntimeException {
	public RoomNotFoundException() {
		super("존재하지 않는 방 입니다.");
	}
}
