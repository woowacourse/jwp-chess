package wooteco.chess.dto;

import wooteco.chess.domain.game.ChessGame;
import wooteco.chess.entity.ChessGameEntity;

public class ChessGameDto {
	private BoardDto boardDto;
	private TurnDto turnDto;
	private StatusDto statusDto;
	private Boolean finished;

	private ChessGameDto(BoardDto boardDto, TurnDto turnDto, StatusDto statusDto, Boolean finished) {
		this.boardDto = boardDto;
		this.turnDto = turnDto;
		this.statusDto = statusDto;
		this.finished = finished;
	}

	public static ChessGameDto from(ChessGameEntity chessGameEntity) {
		ChessGame chessGame = chessGameEntity.toDomain();
		BoardDto boardDto = BoardDto.from(chessGame.board());
		TurnDto turnDto = TurnDto.from(chessGame.turn());
		StatusDto statusDto = StatusDto.from(chessGame.status());
		return new ChessGameDto(boardDto, turnDto, statusDto, chessGame.isFinished());
	}

	public Boolean getFinished() {
		return finished;
	}

	public void setFinished(Boolean finished) {
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
