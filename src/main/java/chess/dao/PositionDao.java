package chess.dao;

import chess.domain.pieces.Piece;
import chess.domain.position.Column;
import chess.domain.position.Row;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PositionDao<T> {

    T save(T position);

    Optional<T> findByColumnAndRowAndBoardId(Column column, Row row, int boardId);

    int saveAll(int boardId);

    Map<T, Piece> findAllPositionsAndPieces(int boardId);

    void deleteAll();
}
