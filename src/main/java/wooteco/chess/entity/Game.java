package wooteco.chess.entity;

import org.springframework.data.annotation.Id;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Game {
	@Id
	private Long id;
	private String name;
	private String uuid;
	private Boolean canContinue;
	private Set<History> histories = new HashSet<>();

	public Game() {
	}

	public Game(String name, String uuid) {
		this.name = name;
		this.uuid = uuid;
	}

	public Game(Long id, String name, String uuid, Boolean canContinue) {
		this.id = id;
		this.name = name;
		this.uuid = uuid;
		this.canContinue = canContinue;
	}

	public Game(String gameName, String uuid, Boolean canContinue) {
		this.name = gameName;
		this.uuid = uuid;
		this.canContinue = canContinue;
	}

	public void addHistory(History history) {
		histories.add(history);
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getUuid() {
		return uuid;
	}

	public Boolean getCanContinue() {
		return canContinue;
	}

	public Set<History> getHistories() {
		return histories;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Game game = (Game) o;
		return Objects.equals(id, game.id) &&
				Objects.equals(name, game.name) &&
				Objects.equals(uuid, game.uuid) &&
				Objects.equals(canContinue, game.canContinue);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, uuid, canContinue);
	}
}
