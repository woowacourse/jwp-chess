package chess.dto;

import chess.domain.piece.Color;
import chess.domain.state.Result;
import chess.domain.state.Status;

import java.util.List;
import java.util.Map;
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
}
