package chess.model.domain.piece;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import util.NullChecker;

public enum Team {
    BLACK("BLACK", "WHITE",
        movableAreas -> movableAreas.stream().map(Direction::reverse).collect(Collectors.toList())),
    WHITE("WHITE", "BLACK", movableAreas -> movableAreas);

    private final String name;
    private final String nextTurnName;
    private final Function<List<Direction>, List<Direction>> directionChanger;

    Team(String name, String nextTurnName,
        Function<List<Direction>, List<Direction>> directionChanger) {
        this.name = name;
        this.nextTurnName = nextTurnName;
        this.directionChanger = directionChanger;
    }

    public static Team of(String name) {
        NullChecker.validateNotNull(name);
        return Arrays.stream(Team.values())
            .filter(color -> color.name.equals(name))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }

    public List<Direction> getChangeDirection(List<Direction> directions) {
        NullChecker.validateNotNull(directions);
        return directionChanger.apply(directions);
    }

    public Team nextTurnIfEmptyMySelf() {
        return Arrays.stream(Team.values())
            .filter(color -> this.nextTurnName.equals(color.name))
            .findFirst()
            .orElse(this);
    }

    public Team previousTurnIfEmptyMySelf() {
        return Arrays.stream(Team.values())
            .filter(color -> color.nextTurnIfEmptyMySelf() == this)
            .findFirst()
            .orElse(this);
    }

    public String getName() {
        return this.name;
    }

}
