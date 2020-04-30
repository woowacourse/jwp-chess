package wooteco.chess.dto.req;

public class MoveRequestDto {
	private final int sourceX;
	private final int sourceY;
	private final int targetX;
	private final int targetY;

	public MoveRequestDto(int sourceX, int sourceY, int targetX, int targetY) {
		this.sourceX = sourceX;
		this.sourceY = sourceY;
		this.targetX = targetX;
		this.targetY = targetY;
	}

	public int getSourceX() {
		return sourceX;
	}

	public int getSourceY() {
		return sourceY;
	}

	public int getTargetX() {
		return targetX;
	}

	public int getTargetY() {
		return targetY;
	}
}
