package chess.database.dao;

import chess.database.dto.BoardDto;
import chess.database.dto.PointDto;
import chess.database.dto.RouteDto;

public interface BoardDao {

    void saveBoard(BoardDto boardDto, int roomId);

    BoardDto readBoard(int roomId);

    void deletePiece(PointDto destination, int roomId);

    void updatePiece(RouteDto routeDto, int roomId);

    void removeBoard(int roomId);
}
