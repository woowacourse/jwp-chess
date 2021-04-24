package chess.domain.dao;

import java.util.List;
import java.util.Optional;

public interface HistoryDao {

    void insert(String name);
    Optional<Integer> findIdByName(String name);
    int delete(String name);
    List<String> selectActive();
    void updateEndState(String id);
}
