package wooteco.chess.domain.board;

import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.PieceType;
import wooteco.chess.domain.position.Position;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {
    private static final int BOARD_SIZE = 64;

    private final Map<Position, Piece> board;
    private boolean isFinished;

    public Board(final Map<Position, Piece> board) {
        if (isNotProperBoardSize(board)) {
            throw new IllegalArgumentException("보드가 제대로 생성되지 못했습니다.");
        }
        this.board = board;
        this.isFinished = false;
    }

    public static Board createLoadedBoard(final List<BoardEntity> boardEntities) {
        Map<Position, Piece> board = new HashMap<>();
        for (BoardEntity boardEntity : boardEntities) {
            board.put(Position.of(boardEntity.getPosition()),
                    Piece.of(PieceType.valueOf(boardEntity.getPiece())));
        }
        return new Board(board);
    }

    public Piece findBy(final Position position) {
        return board.keySet().stream()
                .filter(key -> key.equals(position))
                .map(board::get)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 포지션입니다."));
    }

    public boolean isMovable(final String from, final String to) {
        Position fromPosition = Position.of(from);
        Position toPosition = Position.of(to);

        Piece fromPiece = board.get(fromPosition);
        Piece toPiece = board.get(toPosition);

        changeFlagWhenKingCaptured(toPiece);
        return fromPiece.isMovable(this, fromPosition, toPosition);
    }

    public void changeFlagWhenKingCaptured(final Piece toPiece) {
        if (toPiece.isKing()) {
            isFinished = true;
        }
    }

    private boolean isNotProperBoardSize(final Map<Position, Piece> board) {
        return board.size() != BOARD_SIZE;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public Map<Position, Piece> getBoard() {
        return new HashMap(board);
    }
}
