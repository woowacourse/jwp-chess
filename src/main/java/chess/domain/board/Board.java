package chess.domain.board;

import chess.domain.Color;
import chess.domain.Winner;
import chess.domain.piece.NullPiece;
import chess.domain.piece.Piece;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public final class Board {

    private static final String SAME_COLOR_MOVE_EXCEPTION = "같은 팀 기물이 있는 위치로는 이동할 수 없습니다.";
    private static final String EMPTY_SPACE_EXCEPTION = "이동할 수 있는 기물이 없습니다.";
    private static final String INVALID_TURN_EXCEPTION = "상대 진영의 차례입니다.";
    private static final String INVALID_MOVING_PATH_EXCEPTION = "경로에 기물이 있어 움직일 수 없습니다.";
    private static final int ALL_THE_NUMBER_OF_KING = 2;

    private final Map<Position, Piece> piecesByPosition;

    private Color turn;
    private String name;
    private String password;

    public Board() {
        this.piecesByPosition = BoardInitializer.createBoard();
        this.turn = Color.WHITE;
    }

    public Board(Map<Position, Piece> pieces, Color turn) {
        this.piecesByPosition = pieces;
        this.turn = turn;
    }

    public Board(Map<Position, Piece> pieces, Color turn, String name, String password) {
        this.piecesByPosition = pieces;
        this.turn = turn;
        this.name = name;
        this.password = password;
    }

    public void move(Position beforePosition, Position afterPosition) {
        final Piece piece = piecesByPosition.get(beforePosition);
        validateMovable(beforePosition, afterPosition, piece);

        if (isBlank(afterPosition)) {
            piece.move(beforePosition, afterPosition, moveFunction(beforePosition, afterPosition));
            flipTurnToOpponent();
            return;
        }
        if (isCapturing(piece, afterPosition)) {
            piece.capture(beforePosition, afterPosition, moveFunction(beforePosition, afterPosition));
            flipTurnToOpponent();
            return;
        }
        throw new IllegalArgumentException(SAME_COLOR_MOVE_EXCEPTION);
    }

    private void flipTurnToOpponent() {
        turn = turn.opponentColor();
    }

    private void validateMovable(Position beforePosition, Position afterPosition, Piece piece) {
        if (isBlank(beforePosition)) {
            throw new IllegalArgumentException(EMPTY_SPACE_EXCEPTION);
        }
        if (isInvalidTurn(piece)) {
            throw new IllegalArgumentException(INVALID_TURN_EXCEPTION);
        }
        if (existObstacle(beforePosition, afterPosition, piece)) {
            throw new IllegalArgumentException(INVALID_MOVING_PATH_EXCEPTION);
        }
    }

    private boolean isBlank(Position afterPosition) {
        return piecesByPosition.get(afterPosition).isBlank();
    }

    private boolean isInvalidTurn(final Piece piece) {
        return !piece.isSameColor(turn);
    }

    private boolean existObstacle(final Position beforePosition, final Position afterPosition, final Piece piece) {
        return !piece.isKnight() && !beforePosition.existObstacleToOtherPosition(afterPosition, this::isBlank);
    }

    private Consumer<Piece> moveFunction(Position beforePosition, Position afterPosition) {
        return (piece) -> {
            piecesByPosition.put(afterPosition, piece);
            piecesByPosition.put(beforePosition, new NullPiece());
        };
    }

    private boolean isCapturing(Piece piece, Position afterPosition) {
        return !piece.isSameColorWith(piecesByPosition.get(afterPosition));
    }

    public boolean hasKingCaptured() {
        return ALL_THE_NUMBER_OF_KING != collectKing().size();
    }

    private List<Piece> collectKing() {
        return this.piecesByPosition.values()
                .stream()
                .filter(Objects::nonNull)
                .filter(Piece::isKing)
                .collect(Collectors.toList());
    }

    public double scoreOfBlack() {
        return ScoreCalculator.calculateScoreOfBlack(getPiecesByPosition());
    }

    public double scoreOfWhite() {
        return ScoreCalculator.calculateScoreOfWhite(getPiecesByPosition());
    }

    public Map<Position, Piece> getPiecesByPosition() {
        return Collections.unmodifiableMap(piecesByPosition);
    }

    public boolean hasBlackKingCaptured() {
        return collectKing().stream()
                .noneMatch(Piece::isBlack);
    }

    public boolean hasWhiteKingCaptured() {
        return collectKing().stream()
                .allMatch(Piece::isBlack);
    }

    public Winner findWinner() {
        if (hasBlackKingCaptured()) {
            return Winner.WHITE;
        }
        if (hasWhiteKingCaptured()) {
            return Winner.BLACK;
        }
        return findWinnerByScore();
    }

    private Winner findWinnerByScore() {
        final int compared = Double.compare(scoreOfBlack(), scoreOfWhite());
        if (compared > 0) {
            return Winner.BLACK;
        }
        if (compared < 0) {
            return Winner.WHITE;
        }
        return Winner.DRAW;
    }

    public void delete(String inputPassword) {
        if (!password.equals(inputPassword)) {
            throw new IllegalArgumentException("잘못된 비밀번호 입력입니다.");
        }
        if(turn != Color.END){
            throw new IllegalStateException("종료된 게임만 삭제할 수 있습니다.");
        }
    }

    public Color getTurn() {
        return turn;
    }
}
