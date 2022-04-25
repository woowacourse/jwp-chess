package chess.database.dao;

import chess.database.dto.BoardDto;
import chess.database.dto.PieceDto;
import chess.database.dto.PointDto;
import chess.database.dto.RouteDto;
import java.util.HashMap;
import java.util.Map;

public class FakeBoardDao implements BoardDao {

    private final Map<Integer, Map<PointDto, PieceDto>> memoryDatabase;

    public FakeBoardDao() {
        this.memoryDatabase = new HashMap<>();
    }

    @Override
    public void saveBoard(BoardDto boardDto, int roomId) {
        this.memoryDatabase.put(roomId, new HashMap<>(boardDto.getPointPieces()));
    }

    @Override
    public BoardDto readBoard(int roomId) {
        return new BoardDto(memoryDatabase.get(roomId));
    }

    @Override
    public void deletePiece(PointDto destination, int roomId) {
        Map<PointDto, PieceDto> pointPieces = memoryDatabase.get(roomId);
        pointPieces.remove(destination);
    }

    @Override
    public void updatePiece(RouteDto routeDto, int roomId) {
        Map<PointDto, PieceDto> pointPieces = memoryDatabase.get(roomId);
        PieceDto piece = pointPieces.get(routeDto.getSource());
        pointPieces.remove(routeDto.getSource());
        pointPieces.put(routeDto.getDestination(), piece);
    }

    @Override
    public void removeBoard(int roomId) {
        memoryDatabase.remove(roomId);
    }
}
