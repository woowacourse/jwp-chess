package wooteco.chess.dto;

import static java.util.stream.Collectors.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.board.Row;

public class RowsDtoConverter {

    public static List<LineDto> convertFrom(Board board) {
        Map<Row, LineDto> rows = board.getBoard()
                .entrySet()
                .stream()
                .collect(groupingBy(entry -> entry.getKey().getRow(),    // key(row)
                        () -> new TreeMap<>(Collections.reverseOrder()),    // 리턴타입은 reversed TreeMap
                        mapping(entry -> new GamePieceDto(entry.getValue().getName(), entry.getValue().getPlayerColor(),
                                        entry.getKey()),
                                collectingAndThen(toList(), LineDto::new)))); // value(Line)
        return new ArrayList<>(rows.values());
    }
}
