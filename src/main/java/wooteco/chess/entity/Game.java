package wooteco.chess.entity;

import org.springframework.data.annotation.Id;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Game {
	@Id
	private Long id;
	private String name;
	private Boolean canContinue;
	private Set<History> histories = new HashSet<>();

	public Game() {
	}

	public Game(Long id, String name, Boolean canContinue) {
		this.id = id;
		this.name = name;
		this.canContinue = canContinue;
	}

	public Game(String gameName, Boolean canContinue) {
		this.name = gameName;
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
				Objects.equals(canContinue, game.canContinue);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, canContinue);
	}
}
