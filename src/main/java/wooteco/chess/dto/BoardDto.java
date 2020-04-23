package wooteco.chess.dto;

import wooteco.chess.domain.board.Board;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class BoardDto {
    private final Map<String, String> board;

    public BoardDto(final Board board) {
        this.board = toDto(board);
    }

    private Map<String, String> toDto(Board board) {
        Map<String, String> parseResult = board.get()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(entry -> entry.getKey().toPositionName(),
                        entry -> entry.getValue().toSymbol(),
                        (e1, e2) -> e1,
                        LinkedHashMap::new));
        return Collections.unmodifiableMap(parseResult);
    }

    public Map<String, String> get() {
        return Collections.unmodifiableMap(this.board);
    }
}
