package chess.dao;

import chess.domain.Color;
import chess.domain.board.Position;
import chess.domain.piece.Piece;
import chess.domain.piece.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class MockPieceDao implements PieceDao {

    private Map<Integer, FakePiece> fakePiece = new HashMap<>();

    @Override
    public void save(Map<Position, Piece> board, Long boardId) {
        int index = 1;
        for (Entry<Position, Piece> positionPieceEntry : board.entrySet()) {
            Position position = positionPieceEntry.getKey();
            Piece piece = positionPieceEntry.getValue();
            fakePiece.put(index++,
                    new FakePiece(boardId, position.stringName(), piece.getSymbol(), piece.getColor().name()));
        }
    }

    @Override
    public Map<Position, Piece> load(Long boardId) {
        Map<Position, Piece> pieces = new TreeMap<>();
        for (FakePiece fakePiece : fakePiece.values()) {
            Position position = Position.from(fakePiece.getPosition());
            Type type = Type.from(fakePiece.getType());
            Piece piece = type.makePiece(Color.from(fakePiece.getColor()));
            pieces.put(position, piece);
        }
        return pieces;
    }

    @Override
    public void delete(Long boardId) {
        fakePiece = new HashMap<>();
    }

    @Override
    public void updatePosition(Long boardId, String source, String target) {
        FakePiece sourcePiece = null;
        for (Entry<Integer, FakePiece> fakePieceEntry : fakePiece.entrySet()) {
            if (fakePieceEntry.getValue().getPosition().equals(source)) {
                sourcePiece = fakePieceEntry.getValue();
            }
        }
        final String color = sourcePiece.getColor();
        final String type = sourcePiece.getType();
        for (Entry<Integer, FakePiece> fakePieceEntry : fakePiece.entrySet()) {
            if (fakePieceEntry.getValue().getPosition().equals(target)) {
                Integer key = fakePieceEntry.getKey();
                fakePiece.remove(key);
                fakePiece.put(key, new FakePiece(boardId, target, type, color));
                return;
            }
        }
    }

    @Override
    public void updateAll(Map<Position, Piece> board, Long boardId) {
        fakePiece = new HashMap<>();
        save(board, boardId);
    }
}
