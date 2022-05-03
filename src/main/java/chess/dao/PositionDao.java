package chess.dao;

import chess.domain.pieces.Piece;
import chess.domain.position.Column;
import chess.domain.position.Row;
import chess.entities.PositionEntity;
import java.util.List;
import java.util.Map;

public interface PositionDao<T> {

    PositionEntity save(PositionEntity position);

    PositionEntity getByColumnAndRowAndBoardId(Column column, Row row, int boardId);

    int saveAll(int boardId);

    int getIdByColumnAndRowAndBoardId(Column column, Row row, int boardId);

    Map<T, Piece> findAllPositionsAndPieces(int boardId);

    List<PositionEntity> getPaths(List<PositionEntity> positions, int roomId);

    void deleteAll();
}
