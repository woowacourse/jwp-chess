package chess.dto;

import chess.domain.game.ChessGame;

import java.util.List;
import java.util.stream.Collectors;

public class ChessGameResponseDto {

    private final Long chessGameId;
    private boolean isFinished;
    private List<PieceDto> pieceDtos;
    private String state;

    public ChessGameResponseDto(final Long chessGameId, final ChessGame chessGame) {
        this.chessGameId = chessGameId;
        this.isFinished = chessGame.isFinished();
        this.pieceDtos = chessGame.getBoard().getPieces().stream()
                .map(PieceDto::new)
                .collect(Collectors.toList());
        this.state = chessGame.getState().getValue();
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
}
