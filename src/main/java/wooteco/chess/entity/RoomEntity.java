package wooteco.chess.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.HashSet;
import java.util.Set;

@Table("room")
public class RoomEntity {
	@Id
	private Long id;
	private String name;
	private Set<BoardEntity> boardEntities = new HashSet<>();
	private TurnEntity turnEntity;

	public RoomEntity() {
	}

	public RoomEntity(String name) {
		this.name = name;
	}

	public RoomEntity(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public RoomEntity(RoomEntity roomEntity, TurnEntity turnEntity) {
		this.id = roomEntity.id;
		this.name = roomEntity.name;
		this.boardEntities = new HashSet<>(roomEntity.boardEntities);
		this.turnEntity = turnEntity;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void addBoard(BoardEntity boardEntity) {
		this.boardEntities.add(boardEntity);
	}

	public void deleteAllBoard() {
		this.boardEntities.clear();
	}

	public Set<BoardEntity> getBoardEntities() {
		return boardEntities;
	}

	public TurnEntity getTurnEntity() {
		return turnEntity;
	}

	public boolean hasEmptyBoard() {
		return boardEntities.isEmpty();
	}

	public boolean hasEmptyTurn() {
		return turnEntity == null;
	}
}
