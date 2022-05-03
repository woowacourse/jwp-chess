package chess.repository.dao;

import chess.dto.MoveRequest;
import chess.repository.entity.BoardEntity;
import java.util.List;

public interface BoardDao {

    List<BoardEntity> save(BoardEntity boardEntity);

    List<BoardEntity> findById(long id);

    void updateMove(MoveRequest moveRequest);

    void deleteById(long id);

    void deleteAll();
}
