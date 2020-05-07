package wooteco.chess.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("turn")
public class TurnEntity {
	@Id
	private Long id;

	@Column("teamName")
	private String teamName;

	public TurnEntity() {
	}

	public TurnEntity(String teamName) {
		this.teamName = teamName;
	}

	public TurnEntity(Long id, String teamName) {
		this.id = id;
		this.teamName = teamName;
	}

	public Long getId() {
		return id;
	}

	public String getTeamName() {
		return teamName;
	}
}
