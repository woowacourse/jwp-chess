package wooteco.chess.dto;

import wooteco.chess.domain.game.ChessGame;

public class ChessGameDto {
    private BoardDto boardDto;
    private TurnDto turnDto;
    private StatusDto statusDto;
    private boolean isFinished;

    public ChessGameDto(BoardDto boardDto, TurnDto turnDto, StatusDto statusDto, boolean isFinished) {
        this.boardDto = boardDto;
        this.turnDto = turnDto;
        this.statusDto = statusDto;
        this.isFinished = isFinished;
    }

    public ChessGameDto(ChessGame chessGame) {
        this.boardDto = new BoardDto(chessGame.board());
        this.turnDto = new TurnDto(chessGame.turn());
        this.statusDto = new StatusDto(chessGame.status().getWhiteScore(),
                chessGame.status().getBlackScore(), chessGame.status().getWinner());
        this.isFinished = chessGame.isFinished();
    }

    public boolean getIsFinished() {
        return isFinished;
    }

    public void setIsFinished(boolean isFinished) {
        this.isFinished = isFinished;
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
