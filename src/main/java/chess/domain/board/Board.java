package chess.domain.board;

import chess.domain.piece.Blank;
import chess.domain.piece.Piece;
import chess.domain.piece.Pieces;
import chess.domain.piece.direction.MoveStrategy;
import chess.domain.position.Position;

import chess.domain.position.Source;
import chess.domain.position.Target;
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

    public boolean checkPath(final Source source, final Target target) {
        final List<Position> paths = initializePaths(source, target, chessBoard.get(source.position()));
        if (paths.isEmpty()) {
            return chessBoard.get(source.position()).canMove(target);
        }
        if (hasNoPiecesInPath(paths)) {
            return canPieceMoveToTarget(source.position(), target, paths);
        }
        return false;
    }

    private List<Position> initializePaths(final Source source, final Target target, final Piece piece) {
        if (piece.canMultiMove() && source.hasLinearPath(target.position())) {
            return updatePosition(source.position(), target.position());
        }
        return new ArrayList<>();
    }

    private boolean canPieceMoveToTarget(final Position source, final Target target, final List<Position> paths) {
        if (chessBoard.get(source).isPawn()) {
            return chessBoard.get(source).canMove(target);
        }
        final Piece originPiece = chessBoard.get(source);
        Position changedPosition = paths.get(paths.size() - 1);
        final Piece changedPiece = originPiece.move(new Target(chessBoard.get(changedPosition)));
        return changedPiece.canMove(target);
    }

    private List<Position> updatePosition(final Position source, final Position target) {
        final List<Position> paths = new ArrayList<>();
        final MoveStrategy moveStrategy = source.decideMoveStrategy(target);

        Position nextPosition = source.nextPosition(moveStrategy);
        while (!nextPosition.equals(target)) {
            paths.add(nextPosition);
            nextPosition = nextPosition.nextPosition(moveStrategy);
        }
        return new ArrayList<>(paths);
    }

    private boolean hasNoPiecesInPath(final List<Position> paths) {
        return paths.stream()
            .allMatch(path -> chessBoard.get(path).isBlank());
    }
}
