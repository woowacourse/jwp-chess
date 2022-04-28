package chess.dao;

import chess.domain.pieces.Piece;
import chess.domain.position.Column;
import chess.domain.position.Row;

import chess.entities.ChessPosition;
import java.util.List;
import java.util.Map;

public interface PositionDao<T> {

    ChessPosition save(ChessPosition position);

    ChessPosition getByColumnAndRowAndBoardId(Column column, Row row, int boardId);

    int saveAll(int boardId);

    int getIdByColumnAndRowAndBoardId(Column column, Row row, int boardId);

    Map<T, Piece> findAllPositionsAndPieces(int boardId);

    List<ChessPosition> getPaths(List<ChessPosition> positions, int roomId);

    void deleteAll();
}
