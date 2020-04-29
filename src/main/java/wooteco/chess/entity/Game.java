package wooteco.chess.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("game")
public class Game {
	private @Id Long id;
	private @Column("name") String name;
	private @Column("uuid") String uuid;
	private @Column("can_continue") Boolean canContinue;

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

	public Game(String gameName, String uuid, Boolean i) {
		this.name = gameName;
		this.uuid = uuid;
		this.canContinue = i;
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
}
