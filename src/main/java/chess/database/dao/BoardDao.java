package chess.database.dao;

import chess.database.dto.BoardDto;
import chess.database.dto.PointDto;
import chess.database.dto.RouteDto;

public interface BoardDao {
    void saveBoard(BoardDto boardDto, Long gameId);

    BoardDto findBoardById(Long gameId);

    void deletePiece(PointDto destination, Long gameId);

    void updatePiece(RouteDto routeDto, Long gameId);

    void removeBoard(Long gameId);
}
