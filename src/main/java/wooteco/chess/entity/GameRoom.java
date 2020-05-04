package wooteco.chess.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.LinkedHashSet;
import java.util.Set;

@Table("game_room")
public class GameRoom {

	@Id
	private Long id;
	private String name;
	private Boolean state;
	private Set<GameHistory> gameHistories;

	public GameRoom() {
	}

	public GameRoom(final String name) {
		this.name = name;
		this.state = false;
		this.gameHistories = new LinkedHashSet<>();
	}

	public GameRoom(final GameRoom gameRoom, final boolean state) {
		this.id = gameRoom.id;
		this.name = gameRoom.name;
		this.state = state;
		this.gameHistories = gameRoom.gameHistories;
	}

	public void addGameHistory(GameHistory gameHistory) {
		gameHistories.add(gameHistory);
	}

	public void removeGameHistory(GameHistory gameHistory) {
		gameHistories.remove(gameHistory);
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

	public Set<GameHistory> getGameHistories() {
		return gameHistories;
	}

}
