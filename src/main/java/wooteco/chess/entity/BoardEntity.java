package wooteco.chess.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("board")
public class BoardEntity {
	@Id
	private Long id;

	@Column("position")
	private String position;

	@Column("pieceName")
	private String pieceName;

	public BoardEntity() {
	}

	public BoardEntity(String position, String pieceName) {
		this.position = position;
		this.pieceName = pieceName;
	}

	public BoardEntity(Long id, String position, String pieceName) {
		this.id = id;
		this.position = position;
		this.pieceName = pieceName;
	}

	public BoardEntity(Long id) {
		this.id = id;
	}

	public String getPosition() {
		return position;
	}

	public String getPieceName() {
		return pieceName;
	}
}
