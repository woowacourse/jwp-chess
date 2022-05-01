package chess.repository.dao;

import chess.dto.MoveRequest;
import chess.repository.entity.BoardEntity;
import java.util.List;

public interface BoardDao {

    void save(long id);

    List<BoardEntity> findById(long id);

    void updateMove(MoveRequest moveRequest);

    void deleteById(long id);
}
