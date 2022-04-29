package chess.state;

import java.util.function.Supplier;

public enum StateFactory {
    READY(() -> new Ready()),
    PLAY(() -> new Play()),
    FINISH(() -> new Finish());

    private final Supplier<State> creator;

    StateFactory(Supplier<State> creator) {
        this.creator = creator;
    }

    public State create() {
        return creator.get();
    }
}
