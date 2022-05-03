package chess.dao;

import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.entity.BoardEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FakeBoardDao implements BoardDao {

    private final Map<Long, BoardEntity> boards = new HashMap<>();

    private long id = 0L;

    @Override
    public void savePieces(Map<Position, Piece> board, long roomId) {
        for (Position position : board.keySet()) {
            id++;
            boards.put(id, new BoardEntity(id, position.toSymbol(), board.get(position).getSymbol(), roomId));
        }
    }

    @Override
    public List<BoardEntity> findAllPiece(long roomId) {
        return boards.keySet()
                .stream()
                .filter(key -> boards.get(key).getRoomId() == roomId)
                .map(boards::get)
                .collect(Collectors.toList());
    }

    @Override
    public void updatePosition(String symbol, String destination, long roomId) {
        for (Long idx : boards.keySet()) {
            updateIfAvailable(symbol, destination, roomId, idx);
        }
    }

    private void updateIfAvailable(String symbol, String destination, long roomId, Long idx) {
        if (boards.get(idx).getPosition().equals(destination) && boards.get(idx).getRoomId() == roomId) {
            boards.put(idx, new BoardEntity(id, symbol, destination, roomId));
        }
    }

    @Override
    public void deleteBoard(long roomId) {
        for (Long id : boards.keySet()) {
            removeIfAvailable(roomId, id);
        }
    }

    private void removeIfAvailable(long roomId, Long id) {
        if (boards.get(id).getRoomId() == roomId) {
            boards.remove(id);
        }
    }
}
