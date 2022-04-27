package chess.web.service.fakedao;

import chess.domain.piece.Piece;
import chess.domain.piece.position.Position;
import chess.web.dao.ChessBoardDao;
import java.util.HashMap;
import java.util.Map;

public class FakeChessBoardDao implements ChessBoardDao {

    Map<Position, Piece> repository = new HashMap<>();

    @Override
    public void save(Position position, Piece piece, int roomId) {
        repository.put(position, piece);
    }

    @Override
    public void deleteAll(int roomId) {
        repository.clear();
    }

    @Override
    public Map<Position, Piece> findAll(int roomId) {
        return repository;
    }
}
