package chess.dao;

import chess.dto.BoardDto;
import java.util.List;
import java.util.Map;

public interface BoardDao {

    void update(String position, String piece);

    List<BoardDto> getBoard();

    void reset(Map<String, String> board);
}
