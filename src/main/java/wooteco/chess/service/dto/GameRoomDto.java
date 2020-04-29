package wooteco.chess.service.dto;

import java.util.Objects;

import wooteco.chess.entity.GameRoom;

public class GameRoomDto {

	private Long id;
	private String name;
	private Boolean state;

	private GameRoomDto(final Long id, final String name, final Boolean state) {
		this.id = id;
		this.name = name;
		this.state = state;
	}

	public static GameRoomDto of(final GameRoom gameRoom) {
		Objects.requireNonNull(gameRoom, "게임이 null입니다.");
		return new GameRoomDto(gameRoom.getId(), gameRoom.getName(), gameRoom.getState());
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Boolean getState() {
		return state;
	}

}
