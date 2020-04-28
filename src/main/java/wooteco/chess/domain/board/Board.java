package wooteco.chess.domain.board;

import wooteco.chess.domain.piece.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class Board {

    private static final int PAWN_THRESHOLD = 1;
    private static final int NO_DUPLICATION = 0;

    private final Map<Position, Piece> board;
    private Team currentTurn;

    public Board(final Map<Position, Piece> board, final Team currentTurn) {
        this.board = board;
        this.currentTurn = currentTurn;
    }

    public Piece findPieceOn(final Position position) {
        return board.get(position);
    }

    public void move(final Position start, final Position end) {
        Piece startPiece = board.get(start);
        validateMove(start, end, startPiece);

        board.put(end, startPiece);
        board.put(start, PieceRepository.getBlank());
        currentTurn = currentTurn.reverse();
    }

    private void validateMove(final Position start, final Position end, final Piece startPiece) {
        if (!startPiece.isTeamOf(currentTurn)) {
            throw new IllegalArgumentException("현재 차례가 아닙니다.");
        }
        if (!startPiece.isMovable(generatePath(start, end))) {
            throw new IllegalArgumentException("움직일 수 없습니다.");
        }
    }

    private Path generatePath(final Position start, final Position end) {
        return new Path(findMiddlePositions(start, end), start, end);
    }

    private Map<Position, Piece> findMiddlePositions(final Position start, final Position end) {
        return board.entrySet()
                .stream()
                .filter(entry -> entry.getKey().inBetween(start, end)
                        || entry.getKey().equals(start)
                        || entry.getKey().equals(end))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public double getDefaultScore(final Team team) {
        return board.values()
                .stream()
                .filter(piece -> piece.isTeamOf(team))
                .mapToDouble(Piece::getScore)
                .sum();
    }

    public long countDuplicatedPawns(final Team team) {
        List<Position> pawnPositions = getPawnPositions(team);

        return Arrays.stream(Coordinate.values())
                .mapToLong(coordinate -> countPawnsOnCoordinates(coordinate, pawnPositions))
                .sum();
    }

    private List<Position> getPawnPositions(final Team team) {
        return board.entrySet()
                .stream()
                .filter(entry -> entry.getValue().isTeamOf(team)
                        && entry.getValue() instanceof Pawn)
                .map(entry -> entry.getKey())
                .collect(toList());
    }

    private long countPawnsOnCoordinates(final Coordinate coordinate, final List<Position> pawnPositions) {
        long duplicatedPawns = pawnPositions.stream()
                .filter(position -> position.isOnY(coordinate))
                .count();

        if (duplicatedPawns > PAWN_THRESHOLD) {
            return duplicatedPawns;
        }
        return NO_DUPLICATION;
    }

    public boolean hasKing(final Team team) {
        return board.values()
                .stream()
                .anyMatch(piece -> piece.isTeamOf(team) && piece instanceof King);
    }

    public List<Position> findMovablePositions(final Position start) {
        Piece startPiece = board.get(start);
        if (!startPiece.isTeamOf(currentTurn)) {
            return new ArrayList<>();
        }

        List<Position> movablePositions = new ArrayList<>();
        for (Position position : Position.positions) {
            if (startPiece.isMovable(generatePath(start, position))) {
                movablePositions.add(position);
            }
        }
        return movablePositions;
    }

    public Team getCurrentTurn() {
        return currentTurn;
    }
}
