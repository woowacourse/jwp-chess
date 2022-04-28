package chess.dao;

import java.util.Map;

public interface BoardDao {

    void init(Map<String, String> board, int gameId);

    void update(String position, String piece, int gameId);

    Map<String, String> getBoard();

    void reset(Map<String, String> board);
}
