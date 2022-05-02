package chess.domain.piece;

import static chess.domain.chessboard.position.Direction.*;

import chess.domain.chessboard.position.Direction;
import chess.domain.game.Player;
import chess.domain.chessboard.position.Position;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Knight extends Piece {

    private static final double SCORE = 2.5;

    public Knight(final Player player, final Symbol symbol) {
        super(player, symbol);
    }

    @Override
    public boolean canMove(final Position source, final Position target, final Map<Position, Piece> board) {
        final List<Position> positions = getDirections().stream()
                .filter(source::isInBoardAfterMoved)
                .map(source::createMovablePosition)
                .filter(position -> !board.get(position).isSame(player))
                .collect(Collectors.toUnmodifiableList());
        return positions.contains(target);
    }

    @Override
    protected List<Direction> getDirections() {
        return List.of(KNIGHT_EAST_RIGHT,
                KNIGHT_EAST_LEFT,
                KNIGHT_WEST_RIGHT,
                KNIGHT_WEST_LEFT,
                KNIGHT_SOUTH_RIGHT,
                KNIGHT_SOUTH_LEFT,
                KNIGHT_NORTH_RIGHT,
                KNIGHT_NORTH_LEFT
        );
    }

    @Override
    public double addTo(double score) {
        return score + SCORE;
    }
}
