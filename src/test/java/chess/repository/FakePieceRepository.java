package chess.repository;

import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.web.dao.PieceRepository;
import chess.web.dto.PieceDto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class FakePieceRepository implements PieceRepository {

    private final Map<Integer, PieceData> database = new HashMap<>();
    private int autoIncrementId = 0;

    @Override
    public void save(int boardId, String target, PieceDto pieceDto) {
        autoIncrementId++;
        database.put(autoIncrementId, new PieceData(boardId, target, pieceDto.getColor(), pieceDto.getRole()));
    }

    @Override
    public void saveAll(int boardId, Map<Position, Piece> pieces) {
        for (Position position : pieces.keySet()) {
            save(boardId, position.name(), PieceDto.from(position, pieces.get(position)));
        }
    }

    @Override
    public Optional<PieceDto> findOne(int boardId, String position) {
        return database.values().stream()
                .filter(piece -> piece.getBoardId() == boardId)
                .filter(piece -> piece.getPosition().equals(position))
                .map(pieceData -> new PieceDto(position, pieceData.color, pieceData.role))
                .findAny();
    }

    @Override
    public List<PieceDto> findAll(int boardId) {
        return database.values().stream()
                .map(pieceData -> new PieceDto(pieceData.getPosition(), pieceData.color, pieceData.getRole()))
                .collect(Collectors.toList());
    }

    @Override
    public void updateOne(int boardId, String position, PieceDto pieceDto) {
        deleteOne(boardId, position);
        save(boardId, position, pieceDto);
    }

    @Override
    public void deleteOne(int boardId, String position) {
        Optional<Integer> findId = database.keySet().stream()
                .filter(key -> database.get(key).getPosition().equals(position))
                .filter(key -> database.get(key).getBoardId() == boardId)
                .findAny();
        findId.ifPresent(database::remove);
    }

    @Override
    public void deleteAll(int boardId) {
        List<Integer> keys = database.keySet().stream()
                .filter(key -> database.get(key).getBoardId() == boardId)
                .collect(Collectors.toList());
        for (int key : keys) {
            database.remove(key);
        }
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
