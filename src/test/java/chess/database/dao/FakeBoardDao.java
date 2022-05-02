package chess.database.dao;

import chess.database.dto.BoardDto;
import chess.database.dto.PieceDto;
import chess.database.dto.PointDto;
import chess.database.dto.RouteDto;
import chess.domain.board.Point;
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
    public void deletePiece(Point destination, int roomId) {
        Map<PointDto, PieceDto> pointPieces = memoryDatabase.get(roomId);
        pointPieces.remove(PointDto.of(destination));
    }

    @Override
    public void updatePiece(Point source, Point destination, int roomId) {
        Map<PointDto, PieceDto> pointPieces = memoryDatabase.get(roomId);
        PieceDto piece = pointPieces.get(PointDto.of(source));
        pointPieces.remove(PointDto.of(destination));
        pointPieces.put(PointDto.of(destination), piece);
    }

    @Override
    public void removeBoard(int roomId) {
        memoryDatabase.remove(roomId);
    }
}
