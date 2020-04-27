package wooteco.chess.dto;

public class ChessGameDto {
	private BoardDto boardDto;
	private TurnDto turnDto;
	private StatusDto statusDto;
	private boolean finished;

	public ChessGameDto(BoardDto boardDto, TurnDto turnDto, StatusDto statusDto, boolean finished) {
		this.boardDto = boardDto;
		this.turnDto = turnDto;
		this.statusDto = statusDto;
		this.finished = finished;
	}

	public boolean getFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public BoardDto getBoardDto() {
		return boardDto;
	}

	public void setBoardDto(BoardDto boardDto) {
		this.boardDto = boardDto;
	}

	public TurnDto getTurnDto() {
		return turnDto;
	}

	public void setTurnDto(TurnDto turnDto) {
		this.turnDto = turnDto;
	}

	public StatusDto getStatusDto() {
		return statusDto;
	}

	public void setStatusDto(StatusDto statusDto) {
		this.statusDto = statusDto;
	}
}
