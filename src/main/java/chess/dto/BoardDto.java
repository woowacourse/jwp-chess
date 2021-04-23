package chess.dto;

import chess.domain.board.Board;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class BoardDto {

    private final Map<Position, Piece> board;

    public BoardDto(final Board board) {
        this.board = Position.getPositionCache()
            .stream()
            .collect(Collectors
                .toMap(position -> position, board::findPieceBy));
    }
}
