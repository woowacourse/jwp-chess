package chess.web.service.fakedao;

import chess.domain.piece.Piece;
import chess.domain.piece.position.Position;
import chess.web.dao.ChessBoardDao;
import java.util.HashMap;
import java.util.Map;

public class FakeChessBoardDao implements ChessBoardDao {

    Map<Position, Piece> repository = new HashMap<>();

    @Override
    public void save(Position position, Piece piece) {
        repository.put(position, piece);
    }

    @Override
    public void saveById(int id, Position position, Piece piece) {

    }

    @Override
    public void deleteAll() {
        repository.clear();
    }

    @Override
    public void deleteById(int id) {

    }

    @Override
    public Map<Position, Piece> findAll() {
        return repository;
    }

    @Override
    public Map<Position, Piece> findById(int id) {
        return null;
    }
}
