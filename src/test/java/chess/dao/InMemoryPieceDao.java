package chess.dao;

import chess.model.board.ChessInitializer;
import chess.model.board.Square;
import chess.model.piece.Piece;
import chess.model.piece.PieceType;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InMemoryPieceDao implements PieceDao {
    private final Map<Integer, List<PieceEntity>> boardTable = new HashMap<>();

    @Override
    public void initBoard(int gameId) {
        Map<Square, Piece> board = new ChessInitializer().initPieces();
        List<PieceEntity> pieces = board.keySet().stream()
                .map(square -> new PieceEntity(square.getName(), PieceType.getName(board.get(square)),
                        board.get(square).getColor().name()))
                .collect(Collectors.toList());
        boardTable.put(gameId, pieces);
    }

    @Override
    public List<PieceEntity> getBoardByGameId(int gameId) {
        return boardTable.getOrDefault(gameId, Collections.emptyList());
    }

    @Override
    public void remove(int gameId) {
        boardTable.remove(gameId);
    }

    @Override
    public int update(PieceEntity replacePiece, int gameId) {
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
