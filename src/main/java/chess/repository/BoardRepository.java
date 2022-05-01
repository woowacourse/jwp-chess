package chess.repository;

import chess.domain.board.Board;
import chess.dto.MoveRequest;

public interface BoardRepository {

    void save(long id);

    Board findById(long id);

    void updateMove(MoveRequest moveRequest);

    void deleteById(int id);
}
