package chess.dao;

import java.util.Map;

public interface BoardDao {

    void init(Map<String, String> board);

    void update(String position, String piece);

    Map<String, String> getBoard();

    void reset(Map<String, String> board);
}
