package wooteco.chess.dto.request;

import javax.validation.constraints.NotBlank;

public class MoveRequestDto {
	private Long roomId;
	@NotBlank(message = "출발지 위치가 없습니다.")
	private String source;
	@NotBlank(message = "목적지 위치가 없습니다.")
	private String target;

	public MoveRequestDto() {
	}

	public MoveRequestDto(Long roomId, String source, String target) {
		this.roomId = roomId;
		this.source = source;
		this.target = target;
	}

	public Long getRoomId() {
		return roomId;
	}

	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}
}
