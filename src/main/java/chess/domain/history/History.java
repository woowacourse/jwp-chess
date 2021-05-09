package chess.domain.history;

import chess.domain.Entity;
import chess.domain.board.position.Position;
import chess.domain.manager.State;

import java.util.Objects;

public class History extends Entity<Long> {

    private final Long gameId;
    private final Position source;
    private final Position target;
    private final State state;

    public History(Long gameId, Position source, Position target, State state) {
        this.gameId = gameId;
        this.source = source;
        this.target = target;
        this.state = state;
    }

    public History(Long id, Long gameId, Position source, Position target, State state) {
        super(id);
        this.gameId = gameId;
        this.source = source;
        this.target = target;
        this.state = state;
    }

    public History of(Long gameId, Position source, Position target, State state) {
        validateHistory(gameId, source, target, state);
        return new History(gameId, source, target, state);
    }

    private void validateHistory(Long gameId, Position source, Position target, State state) {
        Objects.requireNonNull(gameId, "gameId는 null일 수 없습니다.");
        Objects.requireNonNull(source, "source 위치는 null일 수 없습니다.");
        Objects.requireNonNull(target, "target 위치는 null일 수 없습니다.");
        Objects.requireNonNull(state, "state는 null일 수 없습니다.");
    }

    public String sourceToString() {
        return this.source.parseString();
    }

    public String targetToString() {
        return this.target.parseString();
    }

    public String turnOwnerName() {
        return this.state.turnOwnerName();
    }

    public int turnNumber() {
        return this.state.turnNumberValue();
    }

    public boolean isPlaying() {
        return this.state.isPlaying();
    }

    public Long gameId() {
        return this.gameId;
    }

    public Position source() {
        return this.source;
    }

    public Position target() {
        return this.target;
    }

    public State state() {
        return this.state;
    }
}
