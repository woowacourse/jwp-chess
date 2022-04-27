package chess.service;

import chess.dao.BoardDao;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceFactory;
import chess.domain.position.Position;
import chess.domain.state.BoardInitialize;
import chess.dto.PieceDto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FakeBoardDaoImpl implements BoardDao {

    HashMap<Long, Map<Position, Piece>> piecesOfRooms = new HashMap<>();

    @Override
    public List<PieceDto> findAll(Long roomId) {
        Map<Position, Piece> pieces = piecesOfRooms.get(roomId);
        return pieces.keySet().stream()
            .map(i -> new PieceDto(i.getPositionToString(), pieces.get(i).getSymbol()))
            .collect(Collectors.toList());
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
        pieces.put(Position.from(position), PieceFactory.create(piece.getSymbol()));
    }
}
