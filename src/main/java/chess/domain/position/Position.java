package chess.domain.position;

public class Position {
    private final String s;
    private final String a;

    public Position(final String s, final String a) {
        this.s = s;
        this.a = a;
    }

    public static Position Blank() {
        return null;
    }

    public String position() {
        return s + a;
    }
}
