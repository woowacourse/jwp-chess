package chess.database.dao;

import chess.database.dto.BoardDto;
import chess.database.dto.RouteDto;
import chess.domain.board.Point;

public interface BoardDao {

    void saveBoard(BoardDto boardDto, int roomId);

    BoardDto readBoard(int roomId);

    void deletePiece(Point destination, int roomId);

    void updatePiece(Point source, Point destination, int roomId);

    void removeBoard(int roomId);
}
