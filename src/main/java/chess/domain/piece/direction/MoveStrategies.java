package chess.domain.piece.direction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MoveStrategies {

    private final List<MoveStrategy> moveStrategies;

    public MoveStrategies(final List<MoveStrategy> moveStrategies) {
        this.moveStrategies = new ArrayList<>(moveStrategies);
    }

    public static MoveStrategies everyMoveStrategies() {
        return new MoveStrategies(Arrays
            .asList(new East(), new West(), new South(), new North(), new Northwest(), new Northeast(), new Southeast(),
                new Southwest()));
    }

    public static MoveStrategies knightMoveStrategies() {
        return new MoveStrategies(Arrays
            .asList(new UpLeft(), new UpRight(), new LeftUp(), new LeftDown(), new DownLeft(), new DownRight(),
                new RightUp(), new RightDown()));
    }

    public static MoveStrategies diagonalMoveStrategies() {
        return new MoveStrategies(Arrays.asList(new Northwest(), new Northeast(), new Southeast(), new Southwest()));
    }

    public static MoveStrategies orthogonalMoveStrategies() {
        return new MoveStrategies(Arrays.asList(new East(), new West(), new South(), new North()));
    }

    public static MoveStrategies pawnMoveStrategies() {
        return new MoveStrategies(Arrays.asList(new North(), new Northeast(), new Northwest(), new InitialPawnNorth()));
    }

    public static MoveStrategy matchedMoveStrategy(final int fileDegree, final int rankDegree) {
        return everyMoveStrategies().moveStrategies.stream()
            .filter(moveStrategy -> moveStrategy.isSameDirection(fileDegree, rankDegree))
            .findFirst()
            .orElse(new Nothing());
    }

    public List<MoveStrategy> strategies() {
        return Collections.unmodifiableList(moveStrategies);
    }
}
