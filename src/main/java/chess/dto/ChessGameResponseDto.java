package chess.dto;

import chess.domain.game.ChessGame;

import java.util.List;
import java.util.stream.Collectors;

public class ChessGameResponseDto {
    private final boolean finished;
    private List<PieceDto> pieceDtos;
    private String state;

    public ChessGameResponseDto(final ChessGame chessGame) {
        pieceDtos = chessGame.getBoard().getPieces().stream()
                .map(PieceDto::new)
                .collect(Collectors.toList());
        state = chessGame.getState().getValue();
        finished = chessGame.isFinished();
    }

    public String getState() {
        return state;
    }

    public List<PieceDto> getPieceDtos() {
        return pieceDtos;
    }

    public boolean isFinished() {
        return finished;
    }

}
