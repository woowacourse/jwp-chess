package wooteco.chess.dto;

import wooteco.chess.domain.game.ScoreResult;
import wooteco.chess.domain.game.Turn;

public class ChessGameDto {
    private Long gameId;
    private BoardDto boardDto;
    private Turn turn;
    private ScoreResult score;
    private boolean normalStatus;

    public ChessGameDto(BoardDto boardDto, Turn turn, ScoreResult score, boolean normalStatus) {
        this.boardDto = boardDto;
        this.turn = turn;
        this.score = score;
        this.normalStatus = normalStatus;
        this.gameId = null; // TODO: 2020/04/29 제거할 방법 생각
    }

    public ChessGameDto(Long gameId, BoardDto boardDto, Turn turn, ScoreResult score, boolean normalStatus) {
        this.gameId = gameId;
        this.boardDto = boardDto;
        this.turn = turn;
        this.score = score;
        this.normalStatus = normalStatus;
    }

    public Long getGameId() {
        return gameId;
    }

    public BoardDto getBoardDto() {
        return boardDto;
    }

    public Turn getTurn() {
        return turn;
    }

    public ScoreResult getScore() {
        return score;
    }

    public boolean isNormalStatus() {
        return normalStatus;
    }
}
