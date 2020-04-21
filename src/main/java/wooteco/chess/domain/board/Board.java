package wooteco.chess.domain.board;

import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.PieceRepository;
import wooteco.chess.domain.piece.Team;

import java.util.Map;

import static java.util.stream.Collectors.toMap;


public class Board {
    private final Map<Position, Piece> board;
    private Team currentTurn;

    public Board(Map<Position, Piece> board, Team currentTurn) {
        this.board = board;
        this.currentTurn = currentTurn;
    }

    public void move(Position start, Position end) {
        Piece startPiece = board.get(start);
        validateMove(start, end, startPiece);

        board.put(end, startPiece);
        board.put(start, PieceRepository.getBlank());
        currentTurn = currentTurn.negate();
    }

    private void validateMove(Position start, Position end, Piece startPiece) {
        if (!startPiece.isTeamOf(currentTurn)) {
            throw new IllegalArgumentException("현재 차례가 아닙니다.");
        }

        if (!startPiece.isMovable(generatePath(start, end))) {
            throw new IllegalArgumentException("움직일 수 없습니다.");
        }
    }

    private Path generatePath(Position start, Position end) {
        return new Path(findMiddlePositions(start, end), start, end);
    }

    private Map<Position, Piece> findMiddlePositions(Position start, Position end) {
        return board.entrySet()
                .stream()
                .filter(entry -> entry.getKey().inBetween(start, end)
                        || entry.getKey().equals(start)
                        || entry.getKey().equals(end))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
