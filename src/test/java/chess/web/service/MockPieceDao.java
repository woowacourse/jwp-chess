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
    public void save(Piece piece, Long boardId) {
        mockDb.put(sequenceId++, convertPieceToFake(piece, boardId));
    }

    @Override
    public void updatePieceByPositionAndBoardId(String type, String team, String position, Long boardId) {
        List<Piece> pieces = findAllByBoardId(boardId);
        List<MockPiece> mockPieces = pieces.stream()
                .map(piece -> convertPieceToFake(piece, boardId))
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
    public Optional<Piece> findByPositionAndBoardId(String position, Long boardId) {
        return mockDb.values().stream()
                .filter(mockPiece -> mockPiece.position.equals(position))
                .filter(mockPiece -> mockPiece.boardId.equals(boardId))
                .map(mockPiece -> PieceFactory.create(mockPiece.position, mockPiece.team, mockPiece.type))
                .findFirst();
    }

    @Override
    public List<Piece> findAllByBoardId(Long boardId) {
        return mockDb.values().stream()
                .filter(mockPiece -> mockPiece.boardId.equals(boardId))
                .map(mockPiece -> PieceFactory.create(mockPiece.position, mockPiece.team, mockPiece.type))
                .collect(Collectors.toList());
    }

    @Override
    public void save(List<Piece> pieces, Long boardId) {
        pieces.stream()
                .map(piece -> convertPieceToFake(piece, boardId)).
                forEach(this::saveFakePiece);
    }

    @Override
    public void deleteByBoardId(Long boardId) {
        mockDb.clear();
    }

    private MockPiece convertPieceToFake(Piece piece, Long boardId) {
        return new MockPiece(boardId, piece.getPosition().name(), piece.getType(), piece.getTeam().value());
    }

    private void saveFakePiece(MockPiece mockPiece) {
        mockDb.put(sequenceId++, mockPiece);
    }

    private static class MockPiece {
        private final Long boardId;
        private final String position;
        private String type;
        private String team;

        private MockPiece(Long boardId, String position, String type, String team) {
            this.boardId = boardId;
            this.position = position;
            this.type = type;
            this.team = team;
        }
    }
}
