package chess.model.gamestatus;

import chess.model.board.Board;
import java.util.Arrays;
import java.util.function.Function;

public enum StatusType {
    READY(Ready.class, board -> new Ready()),
    PLAYING(Playing.class, Playing::new),
    END(End.class, End::new);

    private final Class<? extends Status> statusClass;
    private final Function<Board, Status> statusFunction;

    StatusType(final Class<? extends Status> statusClass,
               final Function<Board, Status> statusFunction) {
        this.statusClass = statusClass;
        this.statusFunction = statusFunction;
    }

    public static Status createStatus(final String statusName, final Board board) {
        return Arrays.stream(values())
                .filter(type -> type.hasSameName(statusName))
                .findFirst()
                .map(type -> type.statusFunction.apply(board))
                .orElseThrow(() -> new IllegalArgumentException("잘못된 상태입니다."));
    }

    public static StatusType findByStatus(final Status status) {
        return Arrays.stream(values())
                .filter(type -> type.isMatched(status.getClass()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("맞는 상태 타입을 찾지 못했습니다."));
    }

    private boolean hasSameName(final String compareName) {
        return this.name().equals(compareName.toUpperCase());
    }

    private boolean isMatched(final Class<? extends Status> compareClass) {
        return this.statusClass.isAssignableFrom(compareClass);
    }
}
