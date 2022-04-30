package chess.dao;

import chess.entity.PieceEntity;
import chess.model.board.ChessInitializer;
import chess.model.board.Square;
import chess.model.piece.Piece;
import chess.model.piece.PieceType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryPieceDao implements PieceDao {
    private final Map<Integer, List<PieceEntity>> boardTable = new HashMap<>();

    @Override
    public void initBoard(Integer gameId) {
        Map<Square, Piece> board = new ChessInitializer().initPieces();
        int id = 1;
        List<PieceEntity> pieces = new ArrayList<>();
        for (Square square : board.keySet()) {
            Piece piece = board.get(square);
            pieces.add(
                    new PieceEntity(id++, gameId, square.getName(),
                            PieceType.getName(piece), piece.getColor().name()));
        }

        boardTable.put(gameId, pieces);
    }

    @Override
    public List<PieceEntity> getBoardByGameId(Integer gameId) {
        return boardTable.getOrDefault(gameId, Collections.emptyList());
    }

    @Override
    public void remove(Integer gameId) {
        boardTable.remove(gameId);
    }

    @Override
    public int update(PieceEntity replacePiece, Integer gameId) {
        List<PieceEntity> pieces = boardTable.get(gameId);
        int affectedRows = 0;
        for (int i = 0; i < pieces.size(); i++) {
            affectedRows += replaceIfSquareEquals(replacePiece, pieces, i);
        }
        return affectedRows;
    }

    private int replaceIfSquareEquals(PieceEntity replacePiece, List<PieceEntity> pieces, int i) {
        if (replacePiece.getSquare().equals(pieces.get(i).getSquare())) {
            pieces.set(i, replacePiece);
            return 1;
        }
        return 0;
    }

    public Map<Integer, List<PieceEntity>> getBoardTable() {
        return boardTable;
    }
}
