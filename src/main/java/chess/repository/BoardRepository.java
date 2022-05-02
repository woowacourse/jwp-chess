package chess.repository;

import chess.domain.board.Board;
import chess.dto.MoveRequest;

public interface BoardRepository {

    Board save(long id);

    Board findById(long id);

    void updateMove(MoveRequest moveRequest);

    void deleteById(long id);

    void deleteAll();
}
