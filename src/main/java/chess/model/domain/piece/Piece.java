package chess.model.domain.piece;

import chess.model.domain.board.CastlingSetting;
import chess.model.domain.board.Square;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import util.NullChecker;

public abstract class Piece {

    private final Team team;
    private final Type type;

    protected Piece(Team team, Type type) {
        NullChecker.validateNotNull(team, type);
        this.team = team;
        this.type = type;
    }

    protected Set<Square> getAllMovableArea(Square square) {
        Set<Square> availableSquares = new HashSet<>();
        int repeatCount = getRepeatCount();
        for (int count = 1; count <= repeatCount; count++) {
            addMovableArea(square, availableSquares, count);
        }
        return availableSquares;
    }

    protected abstract int getRepeatCount();

    private void addMovableArea(Square square, Set<Square> availableSquares,
        int count) {
        for (Direction direction : team.getChangeDirection(type.getDirections())) {
            int fileIncrementBy = direction.getMultiplyFileAddAmount(count);
            int rankIncrementBy = direction.getMultiplyRankAddAmount(count);
            if (square.hasIncreased(fileIncrementBy, rankIncrementBy)) {
                availableSquares
                    .add(square.getIncreasedSquare(fileIncrementBy, rankIncrementBy));
            }
        }
    }

    public abstract Set<Square> findMovableAreas(Square square,
        Map<Square, Piece> board,
        Set<CastlingSetting> castlingElements);

    public Set<Square> findMovableAreas(Square square,
        Map<Square, Piece> board) {
        return findMovableAreas(square, board, new HashSet<>());
    }

    protected Set<Square> findSquaresToRemove(Square square, int fileAddAmount,
        int rankAddAmount) {
        Set<Square> squaresToRemove = new HashSet<>();
        for (int i = 0, file = 0, rank = 0; i < Square.MAX_FILE_AND_RANK_COUNT;
            i++, file += fileAddAmount, rank += rankAddAmount) {
            if (square.hasIncreased(file, rank)) {
                squaresToRemove.add(square.getIncreasedSquare(file, rank));
            }
        }
        squaresToRemove.remove(square);
        return squaresToRemove;
    }

    public boolean isSameTeam(Team team) {
        return this.team == team;
    }

    public boolean isNotSameTeam(Team team) {
        return !isSameTeam(team);
    }

    public boolean isSameTeam(Piece piece) {
        return this.team == piece.team;
    }

    public boolean isSameType(Type type) {
        return this.type == type;
    }

    public double getScore() {
        return type.getScore();
    }
}