package chess.dao;

import chess.entity.GameEntity;
import chess.entity.PieceEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FakePieceDao implements PieceDao {
    private final Map<Integer, Map<String, PieceEntity>> storage;

    public FakePieceDao() {
        this.storage = new HashMap<>();
    }
    @Override
    public void insert(PieceEntity pieceEntity) {
        Map<String, PieceEntity> pieceEntities = storage.getOrDefault(pieceEntity.getGameId(), new HashMap<>());
        pieceEntities.put(pieceEntity.getPosition(), pieceEntity);
        if (!storage.containsKey(pieceEntity.getGameId())) {
            storage.put(pieceEntity.getGameId(), pieceEntities);
        } else {
            storage.replace(pieceEntity.getGameId(), pieceEntities);
        }
    }

    @Override
    public PieceEntity find(PieceEntity pieceEntity) {
        Map<String, PieceEntity> pieceEntities = storage.get(pieceEntity.getGameId());
        return pieceEntities.get(pieceEntity.getPosition());
    }

    @Override
    public List<PieceEntity> findAll(PieceEntity pieceEntity) {
        return new ArrayList<>(storage.get(pieceEntity.getGameId()).values());
    }

    @Override
    public void update(PieceEntity from, PieceEntity to) {
        Map<String, PieceEntity> pieceEntities = storage.get(from.getGameId());
        pieceEntities.replace(from.getPosition(), to);
        storage.replace(to.getGameId(), pieceEntities);
    }

    @Override
    public void delete(PieceEntity pieceEntity) {
        Map<String, PieceEntity> pieceEntities = storage.get(pieceEntity.getGameId());
        pieceEntities.remove(pieceEntity.getPosition());
        storage.replace(pieceEntity.getGameId(), pieceEntities);
    }

    @Override
    public void deleteAll(PieceEntity pieceEntity) {
        storage.remove(pieceEntity.getGameId());
    }
}
