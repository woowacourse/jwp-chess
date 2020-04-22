package wooteco.chess.domain.board;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import wooteco.chess.domain.exception.InvalidMovementException;
import wooteco.chess.domain.piece.EmptyPiece;
import wooteco.chess.domain.piece.GamePiece;
import wooteco.chess.domain.player.PlayerColor;
import wooteco.chess.domain.result.ChessResult;

public class Board {

    private final Map<Position, GamePiece> board;

    private Board(Map<Position, GamePiece> board) {
        this.board = Collections.unmodifiableMap(new TreeMap<>(board));
    }

    static Board of(Map<Position, GamePiece> board) {
        return new Board(board);
    }

    public String searchPath(String sourceInput) {
        Position source = Position.from(sourceInput);
        GamePiece sourcePiece = board.get(source);
        validateSourcePiece(sourcePiece);

        return String.join(",", sourcePiece.searchPaths(this, source));
    }

    public Board move(String sourceInput, String targetInput) {
        validateAll(sourceInput, targetInput);

        Map<Position, GamePiece> board = new HashMap<>(this.board);
        Position source = Position.from(sourceInput);
        Position target = Position.from(targetInput);
        GamePiece sourcePiece = board.get(source);

        board.put(target, sourcePiece);
        board.put(source, EmptyPiece.getInstance());

        return new Board(board);
    }

    private void validateAll(String sourceInput, String targetInput) {
        Position source = Position.from(sourceInput);
        Position target = Position.from(targetInput);

        GamePiece sourcePiece = board.get(source);
        validateSourcePiece(sourcePiece);
        validateColor(source, target);
        sourcePiece.validateMoveTo(this, source, target);
    }

    private void validateSourcePiece(GamePiece sourcePiece) {
        if (sourcePiece.equals(EmptyPiece.getInstance())) {
            throw new InvalidMovementException("기물이 존재하지 않습니다.");
        }
    }

    private void validateColor(Position source, Position target) {
        if (board.get(source).isSameColor(board.get(target))) {
            throw new InvalidMovementException("자신의 말은 잡을 수 없습니다.");
        }
    }

    public boolean isSameColor(GamePiece gamePiece, Position position) {
        return board.get(position).isSameColor(gamePiece);
    }

    public boolean isNotEmpty(Position position) {
        return !EmptyPiece.getInstance().equals(board.get(position));
    }

    public ChessResult calculateResult() {
        return ChessResult.from(board);
    }

    public boolean isKing(String position) {
        GamePiece gamePiece = board.get(Position.from(position));
        return gamePiece.isKing();
    }

    public boolean isBlack(String position) {
        GamePiece gamePiece = board.get(Position.from(position));
        return gamePiece.is(PlayerColor.BLACK);
    }

    public boolean isWhite(String position) {
        GamePiece gamePiece = board.get(Position.from(position));
        return gamePiece.is(PlayerColor.WHITE);
    }

    public Map<Position, GamePiece> getBoard() {
        return Collections.unmodifiableMap(board);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Board))
            return false;
        Board board1 = (Board)o;
        return Objects.equals(board, board1.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(board);
    }
}
