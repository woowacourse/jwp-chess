package chess.repository;

import chess.domain.position.MovePosition;

public interface MoveRepository {
    void move(long chessId, MovePosition movePosition);

    void updateChess(long chessId, String status, String turn);
}
