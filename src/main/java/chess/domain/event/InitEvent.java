package chess.domain.event;

public final class InitEvent extends Event {

    private static final String INIT_DESCRIPTION = "";

    public InitEvent() {
        super(EventType.INIT, INIT_DESCRIPTION);
    }

    public MoveRoute toMoveRoute() {
        throw new UnsupportedOperationException("이동 이벤트가 아닙니다.");
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "InitEvent{}";
    }
}
