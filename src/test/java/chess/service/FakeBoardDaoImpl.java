package chess.service;

import chess.dao.BoardDao;
import chess.domain.piece.Piece;
import chess.domain.piece.Pieces;
import chess.domain.position.Position;
import chess.domain.state.BoardInitialize;
import java.util.HashMap;
import java.util.Map;

public class FakeBoardDaoImpl implements BoardDao {

    HashMap<Long, Map<Position, Piece>> piecesOfRooms = new HashMap<>();

    @Override
    public Map<Position, Piece> findAll(Long roomId) {
        return piecesOfRooms.get(roomId);
    }

    @Override
    public void saveAll(Map<Position, Piece> board, Long roomId) {
        piecesOfRooms.put(roomId, BoardInitialize.create());
    }

    @Override
    public void delete(Long roomId) {
        piecesOfRooms.remove(roomId);
    }

    @Override
    public void updatePosition(String symbol, String position, Long roomId) {
        Map<Position, Piece> pieces = this.piecesOfRooms.get(roomId);
        Piece piece = pieces.get(Position.from(position));
        pieces.put(Position.from(position), Pieces.find(piece.getSymbol()));
    }
}
