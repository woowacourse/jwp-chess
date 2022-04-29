package chess.model.board;

import static chess.model.Team.NONE;

import chess.model.Team;
import chess.model.piece.Blank;
import chess.model.piece.Piece;
import chess.model.position.Position;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Board {

    private static final long DEFAULT_KING_COUNT_IN_BOARD = 2;

    private final Map<Position, Piece> board;

    private Board(final Map<Position, Piece> board) {
        this.board = new HashMap<>(board);
    }

    public static Board init() {
        return new Board(BoardCreator.create());
    }

    public static Board from(final Map<Position, Piece> board) {
        return new Board(board);
    }

    public void checkSameTeam(Team team, Position source) {
        if (pieceOn(source).isOpponentTeam(team)) {
            throw new IllegalArgumentException("[ERROR] 상대편 기물은 움직일 수 없습니다.");
        }
    }

    public void move(final Position source, final Position target) {
        checkPieceIn(source);
        checkPieceCanMove(source, target);
        board.put(target, board.get(source));
        board.put(source, new Blank());
    }

    private void checkPieceIn(final Position source) {
        if (pieceOn(source).isSameTeam(NONE)) {
            throw new IllegalArgumentException("[ERROR] 선택한 위치에 기물이 없습니다.");
        }
    }

    private void checkPieceCanMove(final Position source, final Position target) {
        if (!pieceOn(source).canMove(source, target, board)) {
            throw new IllegalArgumentException("[ERROR] 선택한 기물을 이동 시킬수 없는 위치가 입력 됬습니다.");
        }
    }

    public boolean isKingDead() {
        final long nowKingCount = board.values()
                .stream()
                .filter(Piece::isKing)
                .count();
        return nowKingCount < DEFAULT_KING_COUNT_IN_BOARD;
    }

    public Map<Position, Piece> getBoard() {
        return Collections.unmodifiableMap(board);
    }

    public List<Position> searchPositionOfPawnsFrom(Team team) {
        return board.keySet()
                .stream()
                .filter(position -> pieceOn(position).isSameTeam(team))
                .filter(position -> pieceOn(position).isPawn())
                .collect(Collectors.toList());
    }

    private Piece pieceOn(Position position) {
        return board.get(position);
    }

    public Double calculateDefaultScoreFrom(final Team team) {
        Double defaultScore = board.values()
                .stream()
                .filter(piece -> piece.isSameTeam(team))
                .mapToDouble(Piece::score)
                .sum();
        return defaultScore;
    }
}
