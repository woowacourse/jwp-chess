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
    final Map<String, PieceDto> positionsAndPieces;
    private final Map<Color, Double> whiteScore;
    private final Map<Color, Double> blackScore;
    private final Result result;

    /*public ChessGameDto(final Map<String, List<String>> board, final Status status) {
        positionsAndPieces = new HashMap<>();
        for (final Map.Entry<String, List<String>> entry : board.entrySet()) {
            positionsAndPieces.put(entry.getKey(), new PieceDto(entry.getValue()));
        }
        whiteScore = status.getWhiteScore();
        blackScore = status.getBlackScore();
        result = status.getResult();
    }*/

    public ChessGameDto(final List<BoardElementDto> boardDatas, final Status status) {
        this.positionsAndPieces = boardDatas.stream()
                .collect(Collectors.toMap(BoardElementDto::getPosition, it -> new PieceDto(it.getPieceName(), it.getPieceColor())));
        whiteScore = status.getWhiteScore();
        blackScore = status.getBlackScore();
        result = status.getResult();
    }
}
