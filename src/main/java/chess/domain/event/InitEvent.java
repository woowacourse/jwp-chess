package chess.domain.event;

public final class InitEvent extends Event {

    private static final String INIT_DESCRIPTION = "";

    public boolean isInit() {
        return true;
    }

    public boolean isMove() {
        return false;
    }

    public MoveRoute toMoveRoute() {
        throw new UnsupportedOperationException("이동 이벤트가 아닙니다.");
    }

    public String getType() {
        return EventType.INIT.name();
    }

    public String getDescription() {
        return INIT_DESCRIPTION;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
