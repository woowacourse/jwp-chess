package chess.dto;

import chess.domain.piece.Color;
import chess.domain.state.Result;
import chess.domain.state.Status;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public final class ChessGameDto {

    private final Map<String, PieceDto> positionsAndPieces;
    private final Map<Color, Double> whiteScore;
    private final Map<Color, Double> blackScore;
    private final Result result;

    public ChessGameDto(final List<PieceAndPositionDto> boardDatas, final Status status) {
        this.positionsAndPieces = boardDatas.stream()
                .collect(Collectors.toMap(PieceAndPositionDto::getPosition, it -> new PieceDto(it.getPieceName(), it.getPieceColor())));
        whiteScore = status.getWhiteScore();
        blackScore = status.getBlackScore();
        result = status.getResult();
    }

    public Map<String, PieceDto> getPositionsAndPieces() {
        return positionsAndPieces;
    }

    public Map<Color, Double> getWhiteScore() {
        return whiteScore;
    }

    public Map<Color, Double> getBlackScore() {
        return blackScore;
    }

    public Result getResult() {
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGameDto that = (ChessGameDto) o;
        return Objects.equals(positionsAndPieces, that.positionsAndPieces) && Objects.equals(whiteScore, that.whiteScore) && Objects.equals(blackScore, that.blackScore) && result == that.result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(positionsAndPieces, whiteScore, blackScore, result);
    }
}
