package chess.dto;

import chess.domain.board.Board;
import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceFactory;
import chess.domain.position.Position;
import chess.domain.state.Result;
import chess.domain.state.Status;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class ChessGameDto {
    Map<String, PieceDto> positionsAndPieces;
    private Map<Color, Double> whiteScore;
    private Map<Color, Double> blackScore;
    private Result result;

    /*public ChessGameDto(final Map<String, List<String>> board, final Status status) {
        positionsAndPieces = new HashMap<>();
        for (final Map.Entry<String, List<String>> entry : board.entrySet()) {
            positionsAndPieces.put(entry.getKey(), new PieceDto(entry.getValue()));
        }
        whiteScore = status.getWhiteScore();
        blackScore = status.getBlackScore();
        result = status.getResult();
    }*/
    public ChessGameDto() {}

    public ChessGameDto(final List<BoardElementDto> boardDatas, final Status status) {
        this.positionsAndPieces = boardDatas.stream()
                .collect(Collectors.toMap(BoardElementDto::getPosition, it -> new PieceDto(it.getPieceName(), it.getPieceColor())));
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
}
