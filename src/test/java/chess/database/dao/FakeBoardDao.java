package chess.database.dao;

import java.util.HashMap;
import java.util.Map;

import chess.database.dto.BoardDto;
import chess.database.dto.PieceDto;
import chess.database.dto.PointDto;
import chess.database.dto.RouteDto;

public class FakeBoardDao implements BoardDao {

    private final Map<Long, Map<PointDto, PieceDto>> memoryDatabase;

    public FakeBoardDao() {
        this.memoryDatabase = new HashMap<>();
    }

    @Override
    public void saveBoard(BoardDto boardDto, Long gameId) {
        this.memoryDatabase.put(gameId, new HashMap<>(boardDto.getPointPieces()));
    }

    @Override
    public BoardDto findBoardById(Long gameId) {
        return new BoardDto(memoryDatabase.get(gameId));
    }

    @Override
    public void deletePiece(PointDto destination, Long gameId) {
        Map<PointDto, PieceDto> pointPieces = memoryDatabase.get(gameId);
        pointPieces.remove(destination);
    }

    @Override
    public void updatePiece(RouteDto routeDto, Long gameId) {
        Map<PointDto, PieceDto> pointPieces = memoryDatabase.get(gameId);
        PieceDto piece = pointPieces.get(routeDto.getSource());
        pointPieces.remove(routeDto.getSource());
        pointPieces.put(routeDto.getDestination(), piece);
    }

    @Override
    public void removeBoard(Long gameId) {
        memoryDatabase.remove(gameId);
    }
}
