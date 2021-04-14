package chess.domain.position;

public class Target {
    private final Position position;

    private Target(final Position position) {
        this.position = position;
    }

    public static Target valueOf(final Source source, final Position target) {
        return new Target(target);
    }

    public Position getPosition() {
        return position;
    }
}
