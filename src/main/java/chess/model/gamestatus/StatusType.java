package chess.model.gamestatus;

import chess.model.board.Board;
import java.util.Arrays;
import java.util.function.Function;

public enum StatusType {
    READY(board -> new Ready()),
    PLAYING(Playing::new),
    END(End::new);

    private final Function<Board, Status> statusFunction;

    StatusType(Function<Board, Status> statusFunction) {
        this.statusFunction = statusFunction;
    }

    public static Status createStatus(String statusName, Board board) {
        return Arrays.stream(values())
                .filter(type -> type.hasSameName(statusName))
                .findFirst()
                .map(type -> type.statusFunction.apply(board))
                .orElseThrow(() -> new IllegalArgumentException("잘못된 상태입니다."));
    }

    public static StatusType findByStatus(Status status) {
        return Arrays.stream(values())
                .filter(type -> type.hasSameName(status.getClass().getSimpleName()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("맞는 상태 타입을 찾지 못했습니다."));
    }

    private boolean hasSameName(String compareName) {
        return this.name().equals(compareName.toUpperCase());
    }
}
