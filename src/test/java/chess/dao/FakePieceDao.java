package chess.dao;

import chess.dao.entity.PieceEntity;
import chess.domain.position.Position;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FakePieceDao implements PieceDao {

    private final Map<String, PieceEntity> pieces = new HashMap<>();

    @Override
    public void removeByPosition(Long gameId, Position position) {
        String positionName = position.getName();
        pieces.remove(positionName);
    }

    @Override
    public void removeAll() {
        pieces.clear();
    }

    @Override
    public void saveAll(List<PieceEntity> pieces) {
        for (PieceEntity piece : pieces) {
            save(piece);
        }
    }

    @Override
    public void save(PieceEntity piece) {
        pieces.put(piece.getPosition(), piece);
    }

    @Override
    public List<PieceEntity> findPiecesByGameId(Long gameId) {
        return new ArrayList<>(pieces.values());
    }

    @Override
    public void updatePosition(Long gameId, Position position, Position updatedPosition) {

    }
}
