package util;

import chess.dao.ChessPieceDao;
import chess.domain.chesspiece.ChessPiece;
import chess.domain.chesspiece.Color;
import chess.domain.position.Position;
import chess.dto.ChessPieceMapper;
import chess.dto.response.ChessPieceDto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class FakeChessPieceDao implements ChessPieceDao {

    private final Map<Integer, MockChessPiece> storage = new HashMap<>();
    private int series = 1;

    @Override
    public List<ChessPieceDto> findAllByRoomId(final int roomId) {
        return storage.values()
                .stream()
                .filter(it -> it.roomId == roomId)
                .map(it -> ChessPieceDto.of(it.position, it.chessPiece, it.color))
                .collect(Collectors.toList());
    }

    @Override
    public int deleteByRoomIdAndPosition(final int roomId, final Position position) {
        final List<Integer> deletableIdList = storage.values()
                .stream()
                .filter(it -> it.roomId == roomId)
                .filter(it -> it.position.equals(position))
                .map(it -> it.id)
                .collect(Collectors.toList());

        int deletedRow = 0;
        for (final Integer id : deletableIdList) {
            storage.remove(id);
            deletedRow++;
        }
        return deletedRow;
    }

    @Override
    public int deleteAllByRoomId(final int roomId) {
        final List<Integer> deletableIdList = storage.values()
                .stream()
                .filter(it -> it.roomId == roomId)
                .map(it -> it.id)
                .collect(Collectors.toList());

        int deletedRow = 0;
        for (final Integer id : deletableIdList) {
            storage.remove(id);
            deletedRow++;
        }
        return deletedRow;
    }

    @Override
    public int saveAll(final int roomId, final Map<Position, ChessPiece> pieceByPosition) {
        final List<MockChessPiece> mockChessPieces = pieceByPosition.entrySet()
                .stream()
                .map(it -> new MockChessPiece(series++, roomId, it.getKey(),
                        ChessPieceMapper.toPieceType(it.getValue()), it.getValue()
                        .color()))
                .collect(Collectors.toList());

        int insertedRow = 0;
        for (final MockChessPiece mockChessPiece : mockChessPieces) {
            storage.put(mockChessPiece.id, mockChessPiece);
            insertedRow++;
        }
        return insertedRow;
    }


    @Override
    public int updateByRoomIdAndPosition(final int roomId, final Position from, final Position to) {
        final Optional<MockChessPiece> possibleChessPiece = storage.values()
                .stream()
                .filter(it -> it.roomId == roomId)
                .filter(it -> it.position.equals(from))
                .findFirst();
        if (possibleChessPiece.isEmpty()) {
            return 0;
        }
        final MockChessPiece mockChessPiece = possibleChessPiece.get();
        mockChessPiece.position = to;
        storage.put(mockChessPiece.id, mockChessPiece);
        return 1;
    }

    public void deleteAll() {
        storage.clear();
        series = 1;
    }

    private class MockChessPiece {

        private final int id;
        private final int roomId;
        private final String chessPiece;
        private final Color color;
        private Position position;

        public MockChessPiece(final int id, final int roomId, final Position position, final String chessPiece,
                              final Color color) {
            this.id = id;
            this.roomId = roomId;
            this.position = position;
            this.chessPiece = chessPiece;
            this.color = color;
        }
    }
}
