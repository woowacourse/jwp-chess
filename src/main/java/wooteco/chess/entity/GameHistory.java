package wooteco.chess.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import wooteco.chess.domain.chessGame.ChessCommand;

import java.time.LocalDateTime;
import java.util.Arrays;

@Table("game_history")
public class GameHistory {

	private static final String MOVE_COMMAND = "move";

	@Id
	private Long id;

	@Column("source_position")
	private String sourcePosition;

	@Column("target_position")
	private String targetPosition;

	@Column("created_time")
	private LocalDateTime createdTime;

	@Column("game_room")
	private Long gameRoom;

	public GameHistory() {
	}

	public GameHistory(final String sourcePosition, final String targetPosition, final Long gameRoom) {
		this.sourcePosition = sourcePosition;
		this.targetPosition = targetPosition;
		this.createdTime = LocalDateTime.now();
		this.gameRoom = gameRoom;
	}

	public ChessCommand generateMoveCommand() {
		return ChessCommand.of(Arrays.asList(MOVE_COMMAND, sourcePosition, targetPosition));
	}

	public Long getId() {
		return id;
	}

	public String getSourcePosition() {
		return sourcePosition;
	}

	public String getTargetPosition() {
		return targetPosition;
	}

	public LocalDateTime getCreatedTime() {
		return createdTime;
	}

	public Long getGameRoom() {
		return gameRoom;
	}

}
