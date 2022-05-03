package chess.dao;

import chess.dto.BoardDto;
import java.util.List;
import java.util.Map;

public interface BoardDao {

    void create(Map<String, String> board, int roomId);

    void update(String position, String piece, int roomId);

    List<BoardDto> getBoard(int roomId);

    void reset(Map<String, String> board, int roomId);
}
