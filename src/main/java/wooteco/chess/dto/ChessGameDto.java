package wooteco.chess.dto;

import wooteco.chess.domain.game.ScoreResult;
import wooteco.chess.domain.game.Turn;

public class ChessGameDto {
    private Long gameId;
    private final BoardDto boardDto;
    private final Turn turn;
    private final ScoreResult score;
    private final boolean normalStatus;

    public ChessGameDto(BoardDto boardDto, Turn turn, ScoreResult score, boolean normalStatus) {
        this.boardDto = boardDto;
        this.turn = turn;
        this.score = score;
        this.normalStatus = normalStatus;
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
