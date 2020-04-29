package wooteco.chess.domain.board;

import wooteco.chess.domain.piece.PieceState;
import wooteco.chess.domain.piece.PieceType;
import wooteco.chess.domain.piece.implementation.piece.King;
import wooteco.chess.domain.player.Team;
import wooteco.chess.domain.position.Position;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Board {

    private static final int RUNNING_KING_COUNT = 2;
    private Map<Position, PieceState> board;

    private Board(Map<Position, PieceState> board) {
        this.board = board;
    }

    public static Board of(BoardInitializer boardInitializer) {
        return new Board(boardInitializer.create());
    }

    public static Board of(Map<Position, PieceState> board) {
        return new Board(board);
    }

    public void move(Position source, Position target, Team turn) {
        PieceState sourcePiece = board.get(source);
        validateSource(sourcePiece, turn);
        PieceState piece = sourcePiece.move(target, getBoardState());
        board.remove(source);
        board.put(target, piece);
    }

    public List<Position> getMovablePositions(Position source, Team turn) {
        PieceState sourcePiece = board.get(source);
        validateTurn(sourcePiece, turn);
        return sourcePiece.getMovablePositions(getBoardState());
    }

    public boolean isEnd() {
        return board.values()
                .stream()
                .filter(piece -> piece instanceof King)
                .count() < RUNNING_KING_COUNT;
    }

    public double getScores(Team team) {
        return board.values()
                .stream()
                .filter(value -> team.equals(value.getTeam()))
                .mapToDouble(value -> value.getPoint(getSamePieceTypeStatus(value.getPieceType())))
                .sum();
    }

    public Map<Position, PieceState> getBoard() {
        return Collections.unmodifiableMap(board);
    }

    private void validateSource(PieceState sourcePiece, Team turn) {
        validateExists(sourcePiece);
        validateTurn(sourcePiece, turn);
    }

    private void validateExists(PieceState sourcePiece) {
        if (Objects.isNull(sourcePiece)) {
            throw new IllegalArgumentException("잘못된 위치를 선택하셨습니다.");
        }
    }

    private void validateTurn(PieceState sourcePiece, Team turn) {
        if (!turn.isSameTeam(sourcePiece.getTeam())) {
            throw new IllegalArgumentException("해당 플레이어의 턴이 아닙니다.");
        }
    }

    private BoardSituation getSamePieceTypeStatus(PieceType pieceType) {
        Map<Position, Team> boardState = board.entrySet()
                .stream()
                .filter(entry -> pieceType.isSameType(entry.getValue().getPieceType()))
                .collect(Collectors.toMap(
                        entry -> entry.getKey(),
                        entry -> entry.getValue().getTeam()
                ));
        return BoardSituation.of(boardState);
    }

    private BoardSituation getBoardState() {
        Map<Position, Team> boardState = board.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey(),
                        entry -> entry.getValue().getTeam())
                );
        return BoardSituation.of(boardState);
    }
}
