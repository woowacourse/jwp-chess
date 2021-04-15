package chess.domain.board;

import chess.domain.piece.Piece;
import chess.domain.piece.Pieces;
import chess.domain.position.Position;

import java.util.*;
import java.util.stream.Collectors;

public class Board {

    private final Map<Position, Piece> chessBoard;

    public Board(final Pieces pieces) {
        this(assignPieces(pieces));
    }

    private Board(final Map<Position, Piece> chessBoard) {
        this.chessBoard = new TreeMap(chessBoard);
    }

    public static Board emptyBoard() {
        return new Board(BoardInitializer.emptyBoards());
    }

    private static Map<Position, Piece> assignPieces(final Pieces pieces) {
        final Map<Position, Piece> chessBoard = BoardInitializer.emptyBoards();
        final Map<Position, Piece> collect = pieces.unwrap()
                .stream()
                .collect(Collectors.toMap(Piece::position, piece -> piece));
        chessBoard.putAll(collect);
        return chessBoard;
    }

    public Map<Position, Piece> chessBoard() {
        return Collections.unmodifiableMap(chessBoard);
    }

    public Board put(final Pieces pieces) {
        return new Board(pieces);
    }

    public Board put(final Pieces whitePieces, final Pieces blackPieces) {
        List<Piece> pieces = new ArrayList<>();
        pieces.addAll(whitePieces.pieces());
        pieces.addAll(blackPieces.pieces());
        return new Board(new Pieces(pieces));
    }

    public Piece findPiece(final Position position) {
        return chessBoard.get(position);
    }
}
