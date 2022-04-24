package chess.domain.event;

import chess.domain.board.position.Position;
import java.util.Objects;

public class MoveRoute {

    private static final String DESCRIPTION_DELIMITER = " ";

    private final Position source;
    private final Position target;

    public MoveRoute(String source, String target) {
        this.source = Position.of(source);
        this.target = Position.of(target);
    }

    public static MoveRoute ofEventDescription(String description) {
        String[] positions = description.split(DESCRIPTION_DELIMITER);
        return new MoveRoute(positions[0], positions[1]);
    }

    public Position getSource() {
        return source;
    }

    public Position getTarget() {
        return target;
    }

    public String toDescription() {
        String sourceKey = source.toKey();
        String targetKey = target.toKey();

        return sourceKey + DESCRIPTION_DELIMITER + targetKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MoveRoute that = (MoveRoute) o;
        return Objects.equals(source, that.source)
                && Objects.equals(target, that.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, target);
    }

    @Override
    public String toString() {
        return "MoveCommand{" + "source=" + source + ", target=" + target + '}';
    }
}
