package chess.web.service;

import chess.board.piece.Piece;
import chess.board.piece.PieceFactory;
import chess.web.dao.PieceDao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class MockPieceDao implements PieceDao {
    private final Map<Long, MockPiece> mockDb = new HashMap<>();

    private long sequenceId = 1L;

    public MockPieceDao() {
    }

    @Override
    public void save(Piece piece, Long roomId) {
        mockDb.put(sequenceId++, convertPieceToFake(piece, roomId));
    }

    @Override
    public void updatePieceByPositionAndRoomId(String type, String team, String position, Long roomId) {
        List<MockPiece> mockPieces = mockDb.values().stream()
                .filter(mockPiece -> mockPiece.roomId.equals(roomId))
                .collect(Collectors.toList());

        for (MockPiece mockPiece : mockPieces) {
            if (mockPiece.position.equals(position)) {
                mockPiece.type = type;
                mockPiece.team = team;
                break;
            }
        }
    }

    @Override
    public Optional<Piece> findByPositionAndRoomId(String position, Long roomId) {
        return mockDb.values().stream()
                .filter(mockPiece -> mockPiece.position.equals(position))
                .filter(mockPiece -> mockPiece.roomId.equals(roomId))
                .map(mockPiece -> PieceFactory.create(mockPiece.position, mockPiece.team, mockPiece.type))
                .findFirst();
    }

    @Override
    public List<Piece> findAllByRoomId(Long roomId) {
        return mockDb.values().stream()
                .filter(mockPiece -> mockPiece.roomId.equals(roomId))
                .map(mockPiece -> PieceFactory.create(mockPiece.position, mockPiece.team, mockPiece.type))
                .collect(Collectors.toList());
    }

    @Override
    public void save(List<Piece> pieces, Long roomId) {
        pieces.stream()
                .map(piece -> convertPieceToFake(piece, roomId)).
                forEach(this::saveFakePiece);
    }

    @Override
    public void deleteAllByRoomId(Long roomId) {
        mockDb.clear();
    }

    private MockPiece convertPieceToFake(Piece piece, Long roomId) {
        return new MockPiece(roomId, piece.getPosition().name(), piece.getType(), piece.getTeam().value());
    }

    private void saveFakePiece(MockPiece mockPiece) {
        mockDb.put(sequenceId++, mockPiece);
    }

    private static class MockPiece {
        private final Long roomId;
        private final String position;
        private String type;
        private String team;

        private MockPiece(Long roomId, String position, String type, String team) {
            this.roomId = roomId;
            this.position = position;
            this.type = type;
            this.team = team;
        }
    }
}
