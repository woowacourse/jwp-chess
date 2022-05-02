package chess.configuration;

import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.repository.PieceDao;
import chess.repository.entity.PieceEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class FakePieceDao implements PieceDao {

    private final Map<Integer, PieceData> database = new HashMap<>();
    private int autoIncrementId = 0;

    @Override
    public int save(int boardId, String target, PieceEntity pieceEntity) {
        autoIncrementId++;
        database.put(autoIncrementId,
            new PieceData(
                boardId,
                target.toLowerCase(),
                pieceEntity.getColor(),
                pieceEntity.getRole())
        );
        return autoIncrementId;
    }

    @Override
    public void saveAll(int boardId, Map<Position, Piece> pieces) {
        for (Position position : pieces.keySet()) {
            save(boardId, position.name(), PieceEntity.from(position, pieces.get(position)));
        }
    }

    @Override
    public Optional<PieceEntity> findOne(int boardId, String position) {
        return database.values().stream()
            .filter(piece -> piece.getBoardId() == boardId)
            .filter(piece -> piece.getPosition().equals(position))
            .map(pieceData -> new PieceEntity(position, pieceData.color, pieceData.role))
            .findAny();
    }

    @Override
    public List<PieceEntity> findAll(int boardId) {
        return database.values().stream()
            .map(pieceData -> new PieceEntity(pieceData.getPosition(), pieceData.color, pieceData.getRole()))
            .collect(Collectors.toList());
    }

    @Override
    public void updateOne(int boardId, String afterPosition, PieceEntity pieceEntity) {
        deleteOne(boardId, afterPosition);
        save(boardId, afterPosition, pieceEntity);
    }

    @Override
    public void deleteOne(int boardId, String position) {
        Optional<Integer> findId = database.keySet().stream()
            .filter(key -> database.get(key).getPosition().equals(position))
            .filter(key -> database.get(key).getBoardId() == boardId)
            .findAny();
        findId.ifPresent(database::remove);
    }

    private static class PieceData {
        private final int boardId;
        private final String position;
        private final String color;
        private final String role;

        private PieceData(int boardId, String position, String color, String role) {
            this.boardId = boardId;
            this.position = position;
            this.color = color;
            this.role = role;
        }

        private int getBoardId() {
            return boardId;
        }

        private String getPosition() {
            return position;
        }

        private String getRole() {
            return role;
        }
    }
}
