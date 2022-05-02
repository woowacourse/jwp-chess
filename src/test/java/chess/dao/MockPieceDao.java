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
    public void save(Map<Position, Piece> board, int boardId) {
        int index = 1;
        for (Entry<Position, Piece> positionPieceEntry : board.entrySet()) {
            Position position = positionPieceEntry.getKey();
            Piece piece = positionPieceEntry.getValue();
            fakePiece.put(index++,
                    new FakePiece(1, position.stringName(), piece.getSymbol(), piece.getColor().name()));
        }
    }

    @Override
    public Map<Position, Piece> load(int boardId) {
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
    public boolean existPieces(int boardId) {
        return fakePiece.size() > 0;
    }

    @Override
    public void delete(int boardId) {
        fakePiece = new HashMap<>();
    }

    @Override
    public void updatePosition(String source, String target, int boardId) {
    }
}
