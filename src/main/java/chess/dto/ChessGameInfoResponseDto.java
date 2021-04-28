package chess.dto;

import chess.domain.game.ChessGame;

import java.util.List;
import java.util.stream.Collectors;

public class ChessGameInfoResponseDto {

    private Long chessGameId;
    private boolean isFinished;
    private List<PieceDto> pieceDtos;
    private String state;
    private boolean isReady;
    private boolean isPlaying;
    private boolean isEnd;
    private String title;

    public ChessGameInfoResponseDto(final Long chessGameId, final ChessGame chessGame, final String title) {
        this.chessGameId = chessGameId;
        this.isFinished = chessGame.isFinished();
        pieceDtos = chessGame.getBoard().getPieces().stream()
                .map(PieceDto::new)
                .collect(Collectors.toList());
        this.state = chessGame.getState().getValue();
        this.title = title;
        this.isEnd = state.equals("End");
        this.isPlaying = state.equals("WhiteTurn") || state.equals("BlackTurn");
        this.isReady = state.equals("Ready");
    }

    public Long getChessGameId() {
        return chessGameId;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public List<PieceDto> getPieceDtos() {
        return pieceDtos;
    }

    public String getState() {
        return state;
    }

    public String getTitle() {
        return title;
    }

    public boolean isReady() {
        return isReady;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public boolean isEnd() {
        return isEnd;
    }

}
