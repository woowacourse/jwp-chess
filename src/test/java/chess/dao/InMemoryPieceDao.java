package chess.dao;

import chess.model.board.ChessInitializer;
import chess.model.board.Square;
import chess.model.piece.Piece;
import chess.model.piece.PieceType;
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
        return boardTable.get(gameId);
    }

    @Override
    public void remove(int gameId) {
        boardTable.remove(gameId);
    }

    @Override
    public void update(PieceEntity replacePiece, int gameId) {
        List<PieceEntity> pieces = boardTable.get(gameId);
        for (int i = 0; i < pieces.size(); i++) {
            replaceIfSquareEquals(replacePiece, pieces, i);
        }
    }

    private void replaceIfSquareEquals(PieceEntity replacePiece, List<PieceEntity> pieces, int i) {
        if (replacePiece.getSquare().equals(pieces.get(i).getSquare())) {
            pieces.set(i, replacePiece);
        }
    }

    public Map<Integer, List<PieceEntity>> getBoardTable() {
        return boardTable;
    }
}
