package chess.service.dto;

import chess.domain.piece.Color;
import chess.domain.piece.Pieces;
import java.util.List;
import java.util.stream.Collectors;

public class GameStatusDto {

    private List<PieceStatusDto> pieces;
    private ScoreDto scoreDto;
    private boolean isGameOver;
    private Color winner;

    public GameStatusDto() {
    }

    public GameStatusDto(final Pieces pieces, final ScoreDto scoreDto, final boolean isGameOver, final Color winner) {
        this.pieces = pieces.pieces()
            .stream()
            .map(PieceStatusDto::new)
            .collect(Collectors.toList());
        this.scoreDto = scoreDto;
        this.isGameOver = isGameOver;
        this.winner = winner;
    }

    public List<PieceStatusDto> getPieces() {
        return pieces;
    }

    public ScoreDto getScoreDto() {
        return scoreDto;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public Color getWinner() {
        return winner;
    }
}
