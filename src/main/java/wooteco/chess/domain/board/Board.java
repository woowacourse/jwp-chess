package wooteco.chess.domain.board;

import wooteco.chess.domain.piece.Piece;

import java.util.Map;

import static java.util.stream.Collectors.toMap;


public class Board {
    private final Map<Position, Piece> board;

    public Board(Map<Position, Piece> board) {
        this.board = board;
    }

    public void move(Position start, Position end) {

    }

    private Path generatePath(Position start, Position end) {
        return new Path(findMiddlePositions(start, end), start, end);
    }

    private Map<Position, Piece> findMiddlePositions(Position start, Position end) {
        return board.entrySet()
                .stream()
                .filter(entry -> entry.getKey().inBetween(start, end))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
