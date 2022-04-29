package chess.domain.auth;

public class NullCookie extends PlayerCookie {

    public NullCookie() {
        super(null);
    }

    @Override
    public String toString() {
        return "NullCookie{}";
    }
}
