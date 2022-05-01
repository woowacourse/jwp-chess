package chess.dao;

import chess.domain.pieces.Piece;

import java.util.List;
import java.util.Optional;

public interface PieceDao<T> {

    T save(T piece);

    Optional<Piece> findByPositionId(int positionId);

    int updatePositionId(int sourcePositionId, int targetPositionId);

    int deleteByPositionId(int positionId);

    List<T> getAllByBoardId(int boardId);

    void saveAll(List<Piece> pieces);
}
