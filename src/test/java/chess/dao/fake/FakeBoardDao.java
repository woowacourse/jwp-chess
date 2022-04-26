package chess.dao.fake;

import chess.dao.BoardDao;
import chess.domain.board.Board;
import chess.domain.board.strategy.BoardGenerationStrategy;
import chess.dto.BoardDto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FakeBoardDao implements BoardDao {

    private static Map<String, String> board = new HashMap<>();

    public FakeBoardDao(BoardGenerationStrategy strategy) {
        Board data = new Board();
        data.initBoard(strategy);
        board.putAll(data.toMap());
    }

    @Override
    public void init(Map<String, String> board) {
        this.board.putAll(board);
    }

    @Override
    public void update(String position, String piece) {
        board.put(position, piece);
    }

    @Override
    public List<BoardDto> getBoard() {
        List<BoardDto> data = board.entrySet()
                .stream()
                .map(value -> new BoardDto(value.getKey(), value.getValue()))
                .collect(Collectors.toList());
        return data;
    }

    @Override
    public void reset(Map<String, String> board) {
        this.board.clear();
        this.board.putAll(board);
    }
}
