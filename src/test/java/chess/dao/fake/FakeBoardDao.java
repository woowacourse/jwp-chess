package chess.dao.fake;

import chess.dao.BoardDao;
import chess.dto.BoardDto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FakeBoardDao implements BoardDao {

    private static Map<Integer, Map<String, String>> values = new HashMap<>();

    public FakeBoardDao() {
    }

    @Override
    public void create(Map<String, String> board, int roomId) {
        values.put(roomId, board);
    }

    @Override
    public void update(String position, String piece, int roomId) {
        Map<String, String> value = values.get(roomId);
        value.replace(position, piece);
    }

    @Override
    public List<BoardDto> getBoard(int roomId) {
        Map<String, String> board = values.get(roomId);

        List<BoardDto> data = board.entrySet()
                .stream()
                .map(value -> new BoardDto(value.getKey(), value.getValue()))
                .collect(Collectors.toList());
        return data;
    }

    @Override
    public void reset(Map<String, String> board, int roomId) {
        Map<String, String> value = values.get(roomId);
        value.clear();
        value.putAll(board);
    }
}
