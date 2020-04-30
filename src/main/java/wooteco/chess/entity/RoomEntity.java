package wooteco.chess.entity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("room")
public class RoomEntity {
	@Id
	private String uuid;

	private String name;

	private Set<BoardEntity> pieceEntities = new HashSet<>();
	private Set<TurnEntity> turnEntities = new HashSet<>();

	public RoomEntity(String name) {
		this.name = name;
		this.uuid = UUID.randomUUID().toString();
	}

	public String getUuid() {
		return uuid;
	}

	public String getName() {
		return name;
	}

	public Set<BoardEntity> getPieceEntities() {
		return pieceEntities;
	}

	public Set<TurnEntity> getTurnEntities() {
		return turnEntities;
	}
}
