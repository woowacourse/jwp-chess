package wooteco.chess.domain.board;

import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.PieceType;
import wooteco.chess.domain.piece.Team;
import wooteco.chess.domain.strategy.PiecesInitStrategy;
import wooteco.chess.exception.EmptySourceException;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Board {
    private final Map<Position, Piece> pieces;
    private Team turn;

    public Board(Map<Position, Piece> pieces, Team turn) {
        this.pieces = pieces;
        this.turn = turn;
    }

    public Board(Map<Position, Piece> pieces) {
        this(pieces, Team.WHITE);
    }

    public Board(PiecesInitStrategy piecesInitStrategy, Team turn) {
        this(piecesInitStrategy.init(), turn);
    }

    public Board(PiecesInitStrategy piecesInitStrategy) {
        this(piecesInitStrategy.init(), Team.WHITE);
    }

    public void moveIfPossible(Position source, Position target) {
        Piece pieceToBeMoved = pieces.get(source);
        if (pieceToBeMoved.isEmpty()) {
            throw new EmptySourceException(source.getKey());
        }
        pieceToBeMoved.throwExceptionIfNotMovable(this, source, target);
        move(source, target);
        this.turn = turn.getOppositeTeam();
    }

    public boolean isExistAt(Position position) {
        return !pieces.get(position).isEmpty();
    }

    public void move(Position source, Position target) {
        Piece piece = pieces.get(source);
        pieces.put(source, new Piece(Team.NONE, PieceType.NONE));
        pieces.put(target, piece);
    }

    public boolean isExistAnyPieceAt(List<Position> traces) {
        return traces.stream()
                .anyMatch(this::isExistAt);
    }

    public int forwardMoveAmountOfRank(Position source, Position target) {
        int increaseAmountOfRank = source.increaseAmountOfRank(target);
        return isWhite(source) ? increaseAmountOfRank : -1 * increaseAmountOfRank;
    }

    private boolean isFrontLeft(Position source, Position target) {
        return target == frontLeftOf(source);
    }

    private boolean isFrontRight(Position source, Position target) {
        return target == frontRightOf(source);
    }

    private Position frontLeftOf(Position position) {
        if (turn.isWhite()) {
            return position.at(Direction.NORTH_WEST);
        }
        return position.at(Direction.SOUTH_EAST);
    }

    private Position frontRightOf(Position position) {
        if (turn.isWhite()) {
            return position.at(Direction.NORTH_EAST);
        }
        return position.at(Direction.SOUTH_WEST);
    }

    public boolean isExistEnemyFrontLeft(Position source, Position target) {
        return isExistAt(target)
                && isFrontLeft(source, target)
                && isNotSameTeamBetween(source, target);
    }

    public boolean isExistEnemyFrontRight(Position source, Position target) {
        return isExistAt(target)
                && isFrontRight(source, target)
                && isNotSameTeamBetween(source, target);
    }

    public boolean isFinished() {
        return !pieces.containsValue(Piece.of('k')) || !pieces.containsValue(Piece.of('K'));
    }

    public boolean isNotTurnOf(Position position) {
        return getTeamOf(position).isNotSame(this.turn);
    }

    private Team getTeamOf(Position position) {
        return pieces.get(position).getTeam();
    }

    public boolean isSameTeamBetween(Position position1, Position position2) {
        Piece piece1 = pieces.get(position1);
        Piece piece2 = pieces.get(position2);
        return piece1.isSameTeam(piece2);
    }

    public boolean isSameTeamBetween(Team team, Piece piece) {
        return piece.isSameTeam(team);
    }

    public boolean isNotSameTeamBetween(Position position1, Position position2) {
        return !isSameTeamBetween(position1, position2);
    }

    private boolean isWhite(Position position) {
        return pieces.get(position).isWhite();
    }

    public boolean isTurnWhite() {
        return turn.isWhite();
    }

    public Map<Position, Piece> getPieces() {
        return this.pieces;
    }

    public Piece getPiece(Position position) {
        return pieces.get(position);
    }

    public Team getTurn() {
        return this.turn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        return Objects.equals(pieces, board.pieces);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieces);
    }
}
